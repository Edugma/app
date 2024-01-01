package io.edugma.features.account.performance.bottomSheets

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.screen.BottomSheet
import io.edugma.features.account.domain.model.performance.Performance

@Composable
fun ColumnScope.PerformanceBottomSheetContent(performance: Performance) {
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
        performance.grade.takeIf { !it?.title.isNullOrEmpty() }?.let {
            EdLabel(
                text = performance.grade?.title ?: "",
                iconPainter = painterResource(EdIcons.ic_fluent_album_24_regular),
                style = EdTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
