package com.edugma.core.api.api

open class StaticCrashAnalytics : NullableSingleton<CrashAnalytics>(), CrashAnalytics {
    override fun logException(exception: Throwable) {
        instance?.logException(exception)
    }

    override fun logException(tag: String, message: String, exception: Throwable) {
        instance?.logException(tag, message, exception)
    }

    override fun setUser(user: CrashAnalytics.User) {
        instance?.setUser(user)
    }

    override fun setKey(vararg keys: CrashAnalytics.Key) {
        instance?.setKey(*keys)
    }

    override fun log(message: String, data: Map<String, Any>?) {
        instance?.log(message, data)
    }

    override fun setProperties(vararg properties: Pair<String, String>) {
        instance?.setProperties(*properties)
    }
}
