package com.edugma.core.analytics

import com.edugma.core.api.api.CrashAnalytics

class CompositeCrashAnalytics(
    private val delegates: List<CrashAnalytics>,
) : CrashAnalytics {
    override fun logException(exception: Throwable) {
        delegates.forEach { it.logException(exception) }
    }

    override fun logException(tag: String, message: String, exception: Throwable) {
        delegates.forEach { it.logException(tag, message, exception) }
    }

    override fun setUser(user: CrashAnalytics.User) {
        delegates.forEach { it.setUser(user) }
    }

    override fun setKey(vararg keys: CrashAnalytics.Key) {
        delegates.forEach { it.setKey(*keys) }
    }

    override fun log(message: String, data: Map<String, Any>?) {
        delegates.forEach { it.log(message, data) }
    }

    override fun setProperties(vararg properties: Pair<String, String>) {
        delegates.forEach { it.setProperties(*properties) }
    }
}
