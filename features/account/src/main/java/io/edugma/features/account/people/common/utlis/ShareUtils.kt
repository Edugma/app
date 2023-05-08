package io.edugma.features.account.people.common.utlis

import android.content.Context
import android.content.Intent

fun<T> List<T>.share(context: Context, mapper: (Int, T) -> String) {
    mapIndexed { index: Int, t: T -> mapper(index, t) }.share(context)
}

fun List<String>.share(context: Context) {
    Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, joinToString("\n"))
        type = "text/plain"
    }
        .let { Intent.createChooser(it, null) }
        .also(context::startActivity)
}
