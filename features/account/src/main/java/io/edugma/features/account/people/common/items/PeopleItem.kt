package io.edugma.features.account.people.common.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.divider.EdDivider
import io.edugma.core.designSystem.organism.accountSelector.AccountSelectorVO
import io.edugma.core.designSystem.organism.accountSelector.EdAccountSelector
import io.edugma.core.designSystem.organism.accountSelector.EdAccountSelectorPlaceholder
import io.edugma.features.base.core.utils.ClickListener

@Composable
fun PeopleItem(title: String, description: String, avatar: String?, onClick: ClickListener? = null) {
    Column {
        EdAccountSelector(
            state = AccountSelectorVO(title, description, avatar),
            onClick = onClick,
        )
        EdDivider(
            modifier = Modifier.padding(start = 75.dp, end = 10.dp).padding(vertical = 5.dp),
            thickness = 1.dp
        )
    }
}

@Composable
fun PeopleItemPlaceholder() {
    Column {
        EdAccountSelectorPlaceholder()
        EdDivider(
            modifier = Modifier.padding(start = 75.dp, end = 10.dp).padding(vertical = 5.dp),
            thickness = 1.dp
        )
    }
}
