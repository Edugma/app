package io.edugma.features.account.marks.bottomSheets

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.ui.screen.BottomSheet
import io.edugma.domain.account.model.Performance

@Composable
fun PerformanceBottomSheetContent(performance: Performance) {
    BottomSheet(
        header = performance.name,
        headerStyle = EdTheme.typography.headlineSmall,
        modifier = Modifier.navigationBarsPadding()
    ) {
        EdLabel(text = performance.toString())
    }

}
