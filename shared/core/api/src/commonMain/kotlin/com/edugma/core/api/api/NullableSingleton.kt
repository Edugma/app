package com.edugma.core.api.api

abstract class NullableSingleton<T : Any> {
    protected var instance: T? = null
        private set

    fun setInstance(instance: T) {
        this.instance = instance
    }
}
