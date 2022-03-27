package io.edugma.data.base.store

import io.edugma.data.base.model.Cached
import io.edugma.domain.base.utils.Lce
import io.edugma.domain.base.utils.loading
import io.edugma.domain.base.utils.onSuccess
import kotlinx.coroutines.flow.*
import kotlin.time.Duration

class StoreImpl<Key, Data>(
    private val fetcher: Store<Key, Data>.(key: Key) -> Flow<Result<Data>>,
    private val reader: Store<Key, Data>.(key: Key) -> Flow<Cached<Data?>>,
    private val writer: Store<Key, Data>.(key: Key, data: Data) -> Flow<Result<Unit>>,
    override val expireAt: Duration
): Store<Key, Data> {
    override fun get(key: Key, forceUpdate: Boolean): Flow<Lce<Data?>> =
        reader(key).transform { (data, isExpired) ->
            val needUpdate = isExpired || forceUpdate
            emit(data.map { it!! }.loading(needUpdate))
            if (needUpdate) {
                emitAll(
                    fetcher(key)
                        .onSuccess { newData ->
                            writer(key, newData).collect()
                        }.map { it.loading(false) }
                )
            }
        }
}