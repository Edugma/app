package io.edugma.data.base.store

import io.edugma.core.api.model.CachedResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates
import kotlin.time.Duration

class StoreBuilder<TKey, TData> {
    private var fetch: (suspend Store<TKey, TData>.(key: TKey) -> TData) by Delegates.notNull()
    private var cache: CacheBuilder<TKey, TData> by Delegates.notNull()
    private var coroutineContext: CoroutineContext? = null

    fun fetcher(fetch: suspend Store<TKey, TData>.(key: TKey) -> TData) {
        this.fetch = fetch
    }

    fun cache(build: CacheBuilder<TKey, TData>.() -> Unit) {
        val cacheBuilder = CacheBuilder<TKey, TData>()
        build(cacheBuilder)
        this.cache = cacheBuilder
    }

    fun coroutineScope(context: CoroutineContext = Dispatchers.IO) {
        this.coroutineContext = context
    }

    class CacheBuilder<TKey, TData> {
        internal var read: (suspend Store<TKey, TData>.(key: TKey) -> Flow<CachedResult<TData>?>)
            by Delegates.notNull()
        internal var write: (suspend Store<TKey, TData>.(key: TKey, data: TData) -> Unit)
            by Delegates.notNull()
        internal var expiresIn: Duration by Delegates.notNull()

        fun reader(read: suspend Store<TKey, TData>.(key: TKey) -> Flow<CachedResult<TData>?>) {
            this.read = read
        }

        fun writer(write: suspend Store<TKey, TData>.(key: TKey, data: TData) -> Unit) {
            this.write = write
        }

        fun expiresIn(duration: Duration) {
            this.expiresIn = duration
        }
    }

    @PublishedApi
    internal fun build(): Store<TKey, TData> {
        return StoreImpl(
            fetcher = fetch,
            reader = cache.read,
            writer = cache.write,
            expiresIn = cache.expiresIn,
            coroutineContext = coroutineContext,
        )
    }
}

inline fun <TKey, TData> store(build: StoreBuilder<TKey, TData>.() -> Unit): Store<TKey, TData> {
    val storeBuilder = StoreBuilder<TKey, TData>()
    build(storeBuilder)
    return storeBuilder.build()
}
