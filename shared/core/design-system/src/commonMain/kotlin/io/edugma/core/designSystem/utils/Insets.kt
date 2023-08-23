package io.edugma.core.designSystem.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.statusBarsPadding(): Modifier {
    return composed {
        windowInsetsPadding(WindowInsets.statusBars)
    }
}

fun Modifier.navigationBarsPadding(): Modifier {
    return composed {
        windowInsetsPadding(WindowInsets.navigationBars)
    }
}

fun Modifier.imePadding(): Modifier {
    return composed {
        windowInsetsPadding(WindowInsets.ime)
    }
}
