package com.edugma.data.base.store

import co.touchlab.kermit.Logger
import com.edugma.core.api.api.CrashAnalytics
import com.edugma.core.api.model.CachedResult
import com.edugma.core.api.utils.LceFlow
import com.edugma.core.api.utils.LceFlowCollector
import com.edugma.core.api.utils.lce
import com.edugma.core.api.utils.runCoCatching
import io.ktor.util.collections.ConcurrentMap
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

class StoreImpl<TKey, TData>(
    private val fetcher: suspend Store<TKey, TData>.(key: TKey) -> TData,
    private val reader: suspend Store<TKey, TData>.(key: TKey) -> Flow<CachedResult<TData>?>,
    private val writer: suspend Store<TKey, TData>.(key: TKey, data: TData) -> Unit,
    override val expiresIn: Duration,
    coroutineContext: CoroutineContext?,
) : Store<TKey, TData> {

    private val scope = coroutineContext?.let { CoroutineScope(it) }
    private val currentRequests = ConcurrentMap<TKey, Job>()

    // TODO перейти на то, чтобы читался бесконечно кэш после загрузки
    //  но проверить моменты, как п2р делается и где это может сломать всё
    //  + awaitFinished
    override fun get(key: TKey, forceUpdate: Boolean): LceFlow<TData> {
        Logger.d(
            "Get data from store#${this.hashCode()} " +
                "with key=$key, forceUpdate=$forceUpdate",
            tag = TAG,
        )
        return lce {
            // if forceUpdate we don't read from cache
            val needUpdate = if (forceUpdate) {
                true
            } else {
                readCachedData(key, forceUpdate)
            }

            Logger.d("Need update $needUpdate", tag = TAG)

            if (needUpdate) {
                val res = if (scope == null) {
                    fetchAndSave(key)
                } else {
                    scope.async {
                        fetchAndSaveConcurrent(key)
                    }.await()
                }
                res.onSuccess { newData ->
                    Logger.d("Emitted server data", tag = TAG)
                    emitSuccess(newData, false)
                }.onFailure {
                    CrashAnalytics.logException(
                        message = "Fail to fetch data in store#${this.hashCode()}",
                        exception = it,
                        tag = TAG,
                    )
                    emitFailure(it, false)
                }
            }
        }
    }

    private suspend fun LceFlowCollector<TData>.readCachedData(
        key: TKey,
        forceUpdate: Boolean,
    ): Boolean {
        val cachedDataRes = runCoCatching { reader(key).first() }

        var needUpdate = false

        cachedDataRes.onSuccess { cachedData ->
            needUpdate = cachedData == null || cachedData.isExpired(expiresIn) || forceUpdate
            val data = cachedData?.data
            if (data != null) {
                Logger.d("Emitted cached data", tag = TAG)
                emitSuccess(data, needUpdate)
            } else if (!needUpdate) {
                Logger.d("Not found in cache and cache is not expired", tag = TAG)
                // TODO
                emitFailure(
                    Exception("Not found in cache and cache is not expired"),
                    false,
                )
            }
        }.onFailure {
            // need update if cached data can`t be retrieved
            needUpdate = true
            emitFailure(it, true)
        }
        return needUpdate
    }

    private suspend fun fetchAndSave(key: TKey): Result<TData> {
        return runCoCatching {
            val newData = fetcher(key)
            writer(key, newData)
            newData
        }
    }

    private suspend fun fetchAndSaveConcurrent(key: TKey): Result<TData> {
        val currentJob = currentRequests[key]
        if (currentJob == null) {
            val job = Job()
            currentRequests[key] = job
            val job2 = currentRequests[key]
            if (job2 != null && job2 != job) {
                return joinAndRead(job2, key)
            } else {
                return try {
                    val newData = fetcher(key)
                    writer(key, newData)
                    // Remove from map only after we cached it
                    currentRequests.remove(key)
                    job.complete()
                    Result.success(newData)
                } catch (e: CancellationException) {
                    currentRequests.remove(key)
                    job.cancel()
                    throw e
                } catch (e: Throwable) {
                    currentRequests.remove(key)
                    job.completeExceptionally(e)
                    Result.failure(e)
                }
            }
        } else {
            return joinAndRead(currentJob, key)
        }
    }

    private suspend fun joinAndRead(
        currentJob: Job,
        key: TKey,
    ): Result<TData> {
        // If current job will finish with error then it will throw exception
        // and due using join
        runCoCatching {
            currentJob.join()
        }.onFailure {
            return Result.failure(it)
        }

        // if job have finished with success then
        // we can freely read its data from the cache
        val data = reader(key).first()?.data
        return if (data != null) {
            Result.success(data)
        } else {
            Result.failure(
                IllegalStateException(
                    "Concurrent store error: " +
                        "current job finished with success, but last cached value return null.",
                ),
            )
        }
    }

    companion object {
        private const val TAG = "Store"
    }
}
