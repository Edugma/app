package io.edugma.core.api.model

import io.edugma.core.api.utils.RUID

abstract class ListItemUiModel {
    val listKey: String = RUID.get()
    abstract val listContentType: Any
}
