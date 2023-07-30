package io.edugma.data.base.store

import co.touchlab.kermit.Logger
import io.edugma.core.api.utils.Lce
import io.edugma.core.api.utils.TAG
import io.ktor.util.collections.ConcurrentMap
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration

class StoreImpl<TKey, TData>(
    private val fetcher: suspend Store<TKey, TData>.(key: TKey) -> TData,
    private val reader: suspend Store<TKey, TData>.(key: TKey) -> Flow<TData?>,
    private val writer: suspend Store<TKey, TData>.(key: TKey, data: TData) -> Unit,
    override val expiresIn: Duration,
) : Store<TKey, TData> {

    override fun get(key: TKey, forceUpdate: Boolean): Flow<Lce<TData?>> {
        return flow {
            val cachedData = reader(key).first()
            val needUpdate = cachedData == null || forceUpdate
            emit(Lce(Result.success(cachedData), needUpdate))
            if (needUpdate) {
                fetchAndSave(key).onSuccess { newData ->
                    emit(Lce(Result.success(newData), false))
                }.onFailure {
                    Logger.e("Fail to fetch data", it, tag = this@StoreImpl.TAG)
                }
            }
        }
    }

    private val currentRequests = ConcurrentMap<TKey, Job>()

    private suspend fun fetchAndSave(key: TKey): Result<TData> {
        val currentJob = currentRequests[key]
        if (currentJob == null) {
            val job = Job()
            currentRequests[key] = job
            return try {
                val newData = fetcher(key)
                writer(key, newData)
                // Remove from map only after we cached it
                currentRequests.remove(key)
                job.complete()
                Result.success(newData)
            } catch (e: Throwable) {
                currentRequests.remove(key)
                job.completeExceptionally(e)
                Result.failure(e)
            }
        } else {
            // If current job will finish with error then it will throw exception
            // and due using join
            kotlin.runCatching {
                currentJob.join()
            }.onFailure {
                return Result.failure(it)
            }

            // if job have finished with success then
            // we can freely read its data from the cache
            val data = reader(key).first()
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
}
