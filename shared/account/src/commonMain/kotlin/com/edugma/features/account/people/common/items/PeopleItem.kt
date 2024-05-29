package com.edugma.features.account.people.common.items

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.edugma.core.designSystem.organism.cell.EdCell
import com.edugma.core.designSystem.organism.cell.EdCellPlaceholder
import com.edugma.core.utils.ClickListener

@Composable
fun PeopleItem(title: String, description: String, avatar: String?, onClick: ClickListener? = null) {
    EdCell(
        title = title,
        subtitle = description,
        avatar = avatar,
        modifier = Modifier,
        onClick = onClick,
    )
}

@Composable
fun PeopleItemPlaceholder() {
    EdCellPlaceholder(modifier = Modifier.fillMaxWidth())
}
