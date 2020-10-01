package com.mospolytech.mospolyhelper.data.core.local

import android.content.SharedPreferences

class SharedPreferencesDataSource(
    private val prefs: SharedPreferences
) {
    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return prefs.getInt(key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return prefs.getLong(key, defaultValue)
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return prefs.getFloat(key, defaultValue)
    }

    fun getString(key: String, defaultValue: String): String {
        return prefs.getString(key, defaultValue)!!
    }

    fun getStringSet(key: String, defaultValue: Set<String>): Set<String> {
        return prefs.getStringSet(key, defaultValue)!!
    }


    fun setBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun setInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun setLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    fun setFloat(key: String, value: Float) {
        prefs.edit().putFloat(key, value).apply()
    }

    fun setString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun setStringSet(key: String, value: Set<String>) {
        prefs.edit().putStringSet(key, value).apply()
    }
}