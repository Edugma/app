package io.edugma.features.account.marks.bottomSheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.divider.EdDivider
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.organism.accountSelector.AccountSelectorVO
import io.edugma.core.designSystem.organism.accountSelector.EdAccountSelector
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.core.ui.screen.BottomSheet
import io.edugma.domain.account.model.Performance
import io.edugma.features.account.R
import io.edugma.features.base.core.utils.format

@Composable
fun PerformanceBottomSheetContent(performance: Performance) {
    BottomSheet(
        header = performance.name,
        headerStyle = EdTheme.typography.headlineSmall,
        modifier = Modifier.navigationBarsPadding()
    ) {
        Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            EdLabel(
                text = "Номер ведомости: ${performance.billNum}",
                iconPainter = painterResource(id = EdIcons.ic_fluent_book_24_regular),
                style = EdTheme.typography.bodyLarge
            )
            SpacerHeight(height = 12.dp)
            performance.date?.let {
                EdLabel(
                    text = "Дата проведения: ${it.format()}",
                    iconPainter = painterResource(id = EdIcons.ic_fluent_calendar_ltr_24_regular),
                    style = EdTheme.typography.bodyLarge
                )
                SpacerHeight(height = 12.dp)
            }
            EdLabel(
                text = "${performance.course} курс ${performance.semester} семестр",
                iconPainter = painterResource(id = R.drawable.acc_ic_teacher_24),
                style = EdTheme.typography.bodyLarge
            )
            SpacerHeight(height = 12.dp)
            EdLabel(
                text = performance.examType,
                iconPainter = painterResource(id = EdIcons.ic_fluent_person_note_24_regular),
                style = EdTheme.typography.bodyLarge
            )
            SpacerHeight(height = 12.dp)
            performance.grade.takeIf { it.isNotEmpty() }?.let {
                EdLabel(
                    text = performance.grade,
                    iconPainter = painterResource(id = EdIcons.ic_fluent_album_24_regular),
                    style = EdTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            performance.teacher.takeIf { it.isNotEmpty() }?.let {
                TeacherItem(performance = performance)
            }
        }
    }
}

@Composable
private fun ColumnScope.TeacherItem(performance: Performance) {
    SpacerHeight(height = 10.dp)
    EdDivider(thickness = 1.dp)
    SpacerHeight(height = 10.dp)
    EdLabel(
        text = "Преподаватель",
        style = EdTheme.typography.bodyLarge,
    )
    EdAccountSelector(
        state = AccountSelectorVO(performance.teacher, performance.chair, null),
        onClick = null,
    )
}
