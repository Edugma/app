package com.edugma.core.utils.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

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
