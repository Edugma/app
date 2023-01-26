package io.edugma.data.base.store

import android.util.Log
import io.edugma.data.base.model.Cached
import io.edugma.domain.base.utils.*
import kotlinx.coroutines.flow.*
import kotlin.time.Duration

class StoreImpl<Key, Data>(
    private val fetcher: Store<Key, Data>.(key: Key) -> Flow<Result<Data>>,
    private val reader: Store<Key, Data>.(key: Key) -> Flow<Cached<Data?>>,
    private val writer: Store<Key, Data>.(key: Key, data: Data) -> Flow<Result<Unit>>,
    override val expireAt: Duration,
) : Store<Key, Data> {
    override fun get(key: Key, forceUpdate: Boolean): Flow<Lce<Data?>> =
        reader(key).transform { (data, isExpired) ->
            val needUpdate = isExpired || forceUpdate
            emit(data.loading(needUpdate))
            if (needUpdate) {
                emitAll(
                    fetcher(key)
                        .onSuccess { newData ->
                            writer(key, newData).collect()
                        }.onFailure {
                            Log.e(this@StoreImpl.TAG, "Fail to fetch data", it)
                        }.map { it.loading(false) },
                )
            }
        }
}
