package io.edugma.features.account.web

import android.content.Context
import android.content.res.Configuration
import android.webkit.WebView

private const val AUTH_TOKEN_HEADER = "setAuthToken"

private const val SETTINGS_HEADER = "new-settings"
private const val SETTINGS_DARK_MODE = "\"dark\""
private const val SETTINGS_LIGHT_MODE = "\"light\""

fun WebView.setLkToken(token: String) {
    addLocalStorageValue(AUTH_TOKEN_HEADER, "{\"token\":\"$token\",\"jwt\":null,\"jwt_refresh\":null}")
}

fun WebView.addLocalStorageValue(key: String, value: String) {
    evaluateJavascript("window.localStorage.setItem('$key','$value');", null)
}

fun WebView.updateLkTheme() {
    val isDarkMode = context.isUsingNightModeResources()

    val currentValue = if (isDarkMode) SETTINGS_LIGHT_MODE else SETTINGS_DARK_MODE
    val newValue = if (isDarkMode) SETTINGS_DARK_MODE else SETTINGS_LIGHT_MODE

    val jsCode = "var settings = window.localStorage.getItem('$SETTINGS_HEADER');" +
        "settings = settings.replace(`$currentValue`, `$newValue`);" +
        "window.localStorage.setItem('$SETTINGS_HEADER',settings);"

    evaluateJavascript(jsCode, null)
}

private fun Context.isUsingNightModeResources(): Boolean {
    return when (
        resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK
    ) {
        Configuration.UI_MODE_NIGHT_YES -> true
        Configuration.UI_MODE_NIGHT_NO -> false
        Configuration.UI_MODE_NIGHT_UNDEFINED -> false
        else -> false
    }
}
