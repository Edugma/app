package com.edugma.core.analytics

import co.touchlab.kermit.Logger
import com.edugma.core.api.api.CrashAnalytics

class LogCrashAnalytics : CrashAnalytics {

    override fun logException(exception: Throwable) {
        Logger.e(throwable = exception, messageString = "", tag = TAG)
    }

    override fun logException(tag: String, message: String, exception: Throwable) {
        Logger.e(throwable = exception, messageString = message, tag = tag)
    }

    override fun setUser(user: CrashAnalytics.User) {
        Logger.d(messageString = "Set user $user", tag = TAG)
    }

    override fun setKey(vararg keys: CrashAnalytics.Key) {
        Logger.d(messageString = "Set keys $keys", tag = TAG)
    }

    override fun setProperties(vararg properties: Pair<String, String>) {
        Logger.d(messageString = "Set properties $properties", tag = TAG)
    }

    override fun log(message: String, data: Map<String, Any>?) {
        val resultMessage = buildString {
            append(message)
            append(": ")
            var needSeparator = false
            data?.forEach { (key, value) ->
                append(key)
                append("=")
                append(value)
                if (needSeparator) {
                    append(", ")
                } else {
                    needSeparator = true
                }
            }
        }
        Logger.d(messageString = "Log $resultMessage", tag = TAG)
    }

    private companion object {
        private const val TAG = "LogCrashAnalytics"
    }
}
