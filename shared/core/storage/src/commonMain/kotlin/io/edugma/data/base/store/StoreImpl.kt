package io.edugma.data.base.store

import co.touchlab.kermit.Logger
import io.edugma.core.api.model.CachedResult
import io.edugma.core.api.utils.LceFlow
import io.edugma.core.api.utils.LceFlowCollector
import io.edugma.core.api.utils.TAG
import io.edugma.core.api.utils.lce
import io.edugma.core.api.utils.runCoCatching
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

    override fun get(key: TKey, forceUpdate: Boolean): LceFlow<TData> {
        return lce {
            val needUpdate = readCachedData(key, forceUpdate)

            if (needUpdate) {
                val res = if (scope == null) {
                    fetchAndSave(key)
                } else {
                    scope.async {
                        fetchAndSaveConcurrent(key)
                    }.await()
                }
                res.onSuccess { newData ->
                    emitSuccess(newData, false)
                }.onFailure {
                    Logger.e("Fail to fetch data", it, tag = this@StoreImpl.TAG)
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
                emitSuccess(data, needUpdate)
            } else if (!needUpdate) {
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
}
