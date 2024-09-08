package com.edugma.features.account.performance.bottomSheets

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.atoms.label.EdLabel
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.icons.EdIcons
import com.edugma.core.ui.screen.BottomSheet
import com.edugma.features.account.domain.model.performance.GradePosition
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun ColumnScope.PerformanceBottomSheetContent(performance: GradePosition) {
    BottomSheet(
        title = performance.title,
        headerStyle = EdTheme.typography.headlineSmall,
    ) {
        EdLabel(
            text = performance.description,
            iconPainter = painterResource(EdIcons.ic_fluent_book_24_regular),
            style = EdTheme.typography.bodyMedium,
        )
        SpacerHeight(height = 7.dp)
        EdLabel(
            text = performance.type,
            iconPainter = painterResource(EdIcons.ic_fluent_person_note_24_regular),
            style = EdTheme.typography.bodyMedium,
        )
        SpacerHeight(height = 12.dp)
        performance.grade.takeIf { it?.value != null }?.let {
            EdLabel(
                text = performance.grade?.value?.toString().orEmpty(),
                iconPainter = painterResource(EdIcons.ic_fluent_album_24_regular),
                style = EdTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
