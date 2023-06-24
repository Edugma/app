package io.edugma.data.base.store

import io.edugma.domain.base.utils.Lce
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

interface Store<Key, Data> {
    val expiresIn: Duration
    fun get(key: Key, forceUpdate: Boolean = false): Flow<Lce<Data?>>
}
