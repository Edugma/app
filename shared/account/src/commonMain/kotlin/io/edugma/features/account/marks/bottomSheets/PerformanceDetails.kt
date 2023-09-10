package io.edugma.features.account.marks.bottomSheets

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.api.utils.format
import io.edugma.core.designSystem.atoms.divider.EdDivider
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.organism.accountSelector.AccountSelectorVO
import io.edugma.core.designSystem.organism.accountSelector.EdAccountSelector
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.screen.BottomSheet
import io.edugma.features.account.domain.model.Performance

@Composable
fun PerformanceBottomSheetContent(performance: Performance) {
    BottomSheet(
        header = performance.name,
        headerStyle = EdTheme.typography.headlineSmall,
        verticalContentPadding = 5.dp,
    ) {
        EdLabel(
            text = "Номер ведомости: ${performance.billNum}",
            iconPainter = painterResource(EdIcons.ic_fluent_book_24_regular),
            style = EdTheme.typography.bodyMedium,
        )
        SpacerHeight(height = 7.dp)
        performance.date?.let {
            EdLabel(
                text = "Дата проведения: ${it.format()}",
                iconPainter = painterResource(EdIcons.ic_fluent_calendar_ltr_24_regular),
                style = EdTheme.typography.bodyMedium,
            )
            SpacerHeight(height = 7.dp)
        }
        EdLabel(
            text = "${performance.course} курс ${performance.semester} семестр",
            iconPainter = painterResource(EdIcons.ic_fluent_hat_graduation_24_filled),
            style = EdTheme.typography.bodyMedium,
        )
        SpacerHeight(height = 7.dp)
        EdLabel(
            text = performance.examType,
            iconPainter = painterResource(EdIcons.ic_fluent_person_note_24_regular),
            style = EdTheme.typography.bodyMedium,
        )
        SpacerHeight(height = 12.dp)
        performance.grade.takeIf { it.isNotEmpty() }?.let {
            EdLabel(
                text = performance.grade,
                iconPainter = painterResource(EdIcons.ic_fluent_album_24_regular),
                style = EdTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
        }
        performance.teacher.takeIf { it.isNotEmpty() }?.let {
            SpacerHeight(height = 10.dp)
            EdDivider(thickness = 1.dp)
            SpacerHeight(height = 10.dp)
            TeacherItem(performance = performance)
        }
    }
}

@Composable
private fun TeacherItem(performance: Performance) {
    EdLabel(
        text = "Преподаватель",
        style = EdTheme.typography.bodyLarge,
    )
    EdAccountSelector(
        state = AccountSelectorVO(performance.teacher, performance.chair, null),
        onClick = null,
    )
}
