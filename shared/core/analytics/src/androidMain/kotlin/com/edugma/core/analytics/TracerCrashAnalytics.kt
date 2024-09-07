package com.edugma.core.analytics

import com.edugma.core.api.api.CrashAnalytics
import ru.ok.tracer.Tracer
import ru.ok.tracer.crash.report.TracerCrashReport

class TracerCrashAnalytics : CrashAnalytics {

    override fun logException(exception: Throwable) {
        TracerCrashReport.report(exception)
    }

    override fun logException(tag: String, message: String, exception: Throwable) {
        logException(exception)
    }

    override fun setUser(user: CrashAnalytics.User) {
        Tracer.setUserId(user.id)
    }

    override fun setKey(vararg keys: CrashAnalytics.Key) {
        keys.forEach { key ->
            Tracer.setKey(key.key, key.value)
        }
    }

    override fun setProperties(vararg properties: Pair<String, String>) {
        properties.forEach { (key, value) ->
            Tracer.setCustomProperty(key, value)
        }
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
        TracerCrashReport.log(resultMessage)
    }
}
