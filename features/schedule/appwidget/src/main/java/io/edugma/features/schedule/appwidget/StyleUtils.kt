package io.edugma.features.schedule.appwidget

import androidx.annotation.AttrRes
import androidx.compose.runtime.Composable
import androidx.glance.LocalContext
import io.edugma.features.base.core.utils.getAttributeColorOrNull
import io.edugma.features.base.core.utils.getDimensionAttribute
import io.edugma.features.base.core.utils.getStringAttribute

@Composable
fun getDimensionAttribute(@AttrRes attr: Int, defaultValue: Float = 0f): Float =
    LocalContext.current.theme.getDimensionAttribute(attr, defaultValue)

@Composable
fun getStringAttribute(@AttrRes attr: Int): String? =
    LocalContext.current.theme.getStringAttribute(attr)

@Composable
fun getColorAttribute(@AttrRes attr: Int, defaultValue: Int = 0): Int =
    LocalContext.current.getAttributeColorOrNull(attr) ?: defaultValue
