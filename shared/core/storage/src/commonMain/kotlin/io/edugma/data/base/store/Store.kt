package io.edugma.data.base.store

import io.edugma.core.api.utils.LceFlow
import kotlin.time.Duration

interface Store<Key, Data> {
    val expiresIn: Duration
    fun get(key: Key, forceUpdate: Boolean = false): LceFlow<Data>
}
