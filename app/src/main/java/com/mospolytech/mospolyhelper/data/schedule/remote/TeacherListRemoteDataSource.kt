package com.mospolytech.mospolyhelper.data.schedule.remote

import android.util.Log
import com.beust.klaxon.Klaxon
import com.mospolytech.mospolyhelper.utils.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.URL

class TeacherListRemoteDataSource {
    companion object {
        const val TEACHER_LIST_URL =
            "https://raw.githubusercontent.com/mospolyhelper/up-to-date-information/master/teacher_ids.json"
    }

    suspend fun get(): Map<String, String>? {
        // TODO: Fix
        var res: Map<String, String>? = null
        withContext(Dispatchers.IO) {
            res = try {
                val q = URL(TEACHER_LIST_URL).readText()
                Klaxon().parse<Map<String, String>>(q)
            } catch(e: Exception) {
                Log.e(TAG, "Teacher list downloading and parsing exception", e)
                null
            }
        }
        return res
    }
}