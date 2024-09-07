package com.edugma.core.api.api

interface CrashAnalytics {
    fun logException(exception: Throwable)
    fun logException(tag: String, message: String, exception: Throwable)
    fun setUser(user: User)
    fun setKey(vararg keys: Key)
    fun log(message: String, data: Map<String, Any>? = null)
    fun setProperties(vararg properties: Pair<String, String>)

    sealed class Key(val key: String, val value: String) {
        data class CurrentScreen(val name: String): Key(key = "CurrentScreen", value = name)
    }
    data class User(
        val id: String
    )

    companion object : StaticCrashAnalytics()
}
