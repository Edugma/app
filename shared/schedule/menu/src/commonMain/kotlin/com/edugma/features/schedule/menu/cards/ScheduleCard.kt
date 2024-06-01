package com.edugma.features.schedule.menu.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

import edugma.shared.core.resources.generated.resources.Res
import edugma.shared.core.resources.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import com.edugma.core.designSystem.atoms.lottie.EdLottie
import com.edugma.core.designSystem.atoms.lottie.LottieSource
import com.edugma.core.designSystem.organism.actionCard.EdActionCard
import com.edugma.core.designSystem.organism.actionCard.EdActionCardWidth
import com.edugma.core.designSystem.utils.rememberCachedIconPainter
import com.edugma.core.icons.EdIcons
import com.edugma.core.utils.ClickListener
import com.edugma.features.schedule.menu.presentation.ScheduleMenuUiState
import org.jetbrains.compose.resources.painterResource

@Composable
fun ScheduleCard(
    state: ScheduleMenuUiState.MainUiState,
    onScheduleClick: ClickListener,
    modifier: Modifier = Modifier,
) {
    val count = state.todayLessonsCount
    val subtitle = when {
        count < 0 -> ""
        count == 0 -> "Сегодня нет занятий"
        else -> {
            val lessonText = chooseDeclension(number = count, forms = lessonTextList)
            "Сегодня ${count} ${lessonText}"
        }
    }
    EdActionCard(
        title = stringResource(Res.string.sch_schedule),
        subtitle = subtitle,
        onClick = onScheduleClick,
        width = EdActionCardWidth.large,
        modifier = modifier,
    ) {
        if (count == 0) {
            Box (Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
                EdLottie(
                    lottieSource = LottieSource.FileRes("files/sch_relax_2.json"),
                    modifier = Modifier
                        .width(100.dp),
                    contentScale = ContentScale.FillWidth,
                )
            }
        } else {
            Icon(
                painter = rememberCachedIconPainter(
                    "https://img.icons8.com/fluency/48/calendar--v1.png",
                ),
                tint = Color.Unspecified,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
            )
        }
    }
}

private val lessonTextList = listOf(
    "занятие",
    "занятия",
    "занятий",
)

/**
 * Выбирает правильное склонение слова в зависимости от числа.
 *
 * @param number Число, для которого нужно выбрать склонение. Должно быть больше 0.
 * @param forms Массив из трех строк, содержащий слово в разных склонениях:
 *              - forms[0]: форма слова для чисел, оканчивающихся на 1 (но не 11).
 *              - forms[1]: форма слова для чисел, оканчивающихся на 2, 3, 4 (но не 12, 13, 14).
 *              - forms[2]: форма слова для всех остальных чисел.
 * @return Правильное склонение слова в зависимости от переданного числа.
 *
 * @throws IllegalArgumentException если массив forms не содержит ровно три элемента.
 */
fun chooseDeclension(number: Int, forms: List<String>): String {
    require(forms.size == 3) { "Массив forms должен содержать ровно три элемента." }

    val n = number % 100
    return when {
        n in 11..19 -> forms[2]
        n % 10 == 1 -> forms[0]
        n % 10 in 2..4 -> forms[1]
        else -> forms[2]
    }
}
