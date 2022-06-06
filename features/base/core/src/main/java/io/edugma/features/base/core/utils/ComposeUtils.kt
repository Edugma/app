package io.edugma.features.base.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
fun getContext() = LocalContext.current

@Composable
fun TextUnit.dp() =
    with(LocalDensity.current) {
        this@dp.toDp()
    }

@Composable
fun Dp.sp() =
    with(LocalDensity.current) {
        this@sp.toSp()
    }

@Composable
fun Int.pxToDp() =
    with(LocalDensity.current) {
        this@pxToDp.toDp()
    }

@Composable
fun Float.pxToDp() =
    with(LocalDensity.current) {
        this@pxToDp.toDp()
    }