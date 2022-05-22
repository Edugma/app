package io.edugma.data.base.store

import io.edugma.data.base.model.Cached
import io.edugma.domain.base.utils.Lce
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

interface Store<Key, Data> {
    val expireAt: Duration
    fun get(key: Key, forceUpdate: Boolean = false): Flow<Lce<Data?>>

    companion object {
        operator fun <Key, Data> invoke(
            fetcher: Store<Key, Data>.(key: Key) -> Flow<Result<Data>>,
            reader: Store<Key, Data>.(key: Key) -> Flow<Cached<Data?>>,
            writer: Store<Key, Data>.(key: Key, data: Data) -> Flow<Result<Unit>>,
            expireAt: Duration
        ): Store<Key, Data> {
            return StoreImpl(
                fetcher = fetcher,
                reader = reader,
                writer = writer,
                expireAt = expireAt
            )
        }
    }
}