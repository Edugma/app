package io.edugma.core.api.model

import io.edugma.core.api.utils.RUID

abstract class ListItemUiModel {
    val listKey: Any = RUID.get()
    abstract val listContentType: Any
}

fun <T : ListItemUiModel> List<T>.key(): (T) -> Any {
    return { it.listKey }
}

fun <T : ListItemUiModel> List<T>.keyFromIndex(): (Int) -> Any {
    return { this[it].listKey }
}

fun <T : ListItemUiModel> List<T>.contentType(): (T) -> Any {
    return { it.listContentType }
}

fun <T : ListItemUiModel> List<T>.contentTypeFromIndex(): (Int) -> Any {
    return { this[it].listContentType }
}
