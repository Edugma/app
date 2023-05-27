package io.edugma.data.base.store

import android.util.Log
import io.edugma.domain.base.utils.Lce
import io.edugma.domain.base.utils.TAG
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import kotlin.time.Duration

class StoreImpl<TKey, TData>(
    private val fetcher: suspend Store<TKey, TData>.(key: TKey) -> TData,
    private val reader: suspend Store<TKey, TData>.(key: TKey) -> Flow<TData?>,
    private val writer: suspend Store<TKey, TData>.(key: TKey, data: TData) -> Unit,
    override val expiresIn: Duration,
) : Store<TKey, TData> {
    override fun get(key: TKey, forceUpdate: Boolean): Flow<Lce<TData?>> {
        return flow {
            emitAll(
                reader(key)
                    .transform { data ->
                        val isExpired = data == null
                        val needUpdate = isExpired || forceUpdate
                        emit(Lce(Result.success(data), needUpdate))
                        if (needUpdate) {
                            runCatching {
                                fetcher(key)
                            }.onSuccess { newData ->
                                writer(key, newData)
                                emit(Lce(Result.success(newData), false))
                            }.onFailure {
                                Log.e(this@StoreImpl.TAG, "Fail to fetch data", it)
                            }
                        }
                    },
            )
        }
    }
}
