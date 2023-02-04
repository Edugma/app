package io.edugma.features.schedule.appwidget.currentLessons

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import io.edugma.features.schedule.appwidget.R
import io.edugma.features.schedule.domain.model.lesson.Lesson
import io.edugma.features.schedule.domain.model.lesson.LessonTime
import io.edugma.features.schedule.domain.usecase.getShortName
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CurrentLessonAppWidget : GlanceAppWidget(), KoinComponent {

    companion object {
        private val Small = DpSize(104.dp, 155.dp)
        private val Big = DpSize(180.dp, 155.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        setOf(Small, Big),
    )

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    private val viewModel: CurrentLessonViewModel by inject()

    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    override fun Content() {
        val prefs = currentState<Preferences>()
        val lessonIndex = prefs[lessonIndexPreferenceKey] ?: 0

        viewModel.onLessonIndex(lessonIndex)

        CurrentLessonContent(viewModel.state.value)
    }
}

@SuppressLint("PrivateResource")
@Composable
private fun CurrentLessonContent(state: CurrentLessonState) {
    val mod = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        GlanceModifier
            .cornerRadius(20.dp)
            .background(com.google.android.material.R.color.m3_sys_color_dynamic_dark_surface_variant)
    } else {
        GlanceModifier
            .background(ImageProvider(R.drawable.schedule_app_shape_background))
    }
    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .height(130.dp)
            .appWidgetBackground()
            .padding(10.dp)
            .then(mod),
    ) {
//        Column(
//            modifier = GlanceModifier
//                .height(84.dp)
//                .width(139.dp)
//                .background(Color.Red)
//        ) {
        Box(
            modifier = GlanceModifier.defaultWeight(),
        ) {
            if (state.currentLesson == null || state.time == null) {
                NoLessons()
            } else {
                LessonContent(
                    time = state.time,
                    lesson = state.currentLesson,
                )
            }
        }
        Row(GlanceModifier.fillMaxWidth()) {
            if (state.currentLessons.isNotEmpty()) {
                LessonSelector(
                    currentIndex = state.currentLessonIndex,
                    lessonsCount = state.currentLessons.size,
                    modifier = GlanceModifier.defaultWeight(),
                )
            }
            if (state.lastUpdateDateTime != null) {
                Timestamp(
                    updateDateTime = state.lastUpdateDateTime,
                    modifier = GlanceModifier.defaultWeight(),
                )
            }
        }
    }
}

@Composable
private fun NoLessons() {
    Box(
        GlanceModifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Сегодня нет занятий",
        )
    }
}

private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

@Composable
private fun LessonContent(time: LessonTime, lesson: Lesson) {
    Column {
        Row {
            val timeFrom = time.start.format(timeFormat)
            val timeTo = time.end.format(timeFormat)
            Text(
                text = "$timeFrom - $timeTo",
                style = TextStyle(
                    fontSize = 10.sp,
                ),
                maxLines = 1,
            )
            Spacer(GlanceModifier.width(8.dp).height(1.dp))
            Text(
                text = lesson.type.title,
                style = TextStyle(
                    fontSize = 10.sp,
                    color = ColorProvider(R.color.appwidget_onSecondaryContainer),
                ),
                maxLines = 1,
                modifier = GlanceModifier
                    .background(R.color.appwidget_secondaryContainer)
                    .padding(horizontal = 3.dp),
            )
        }
        Text(
            text = lesson.subject.title,
            style = TextStyle(
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
            ),
            maxLines = 2,
        )
        Spacer(GlanceModifier.width(1.dp).height(1.dp))
        Row {
            Image(
                provider = ImageProvider(
                    R.drawable.schedule_app_ic_fluent_hat_graduation_12_regular,
                ),
                contentDescription = null,
                modifier = GlanceModifier.size(12.dp),
            )
            Spacer(GlanceModifier.width(3.dp).height(1.dp))
            Text(
                text = lesson.teachers.joinToString { it.getShortName() },
                style = TextStyle(
                    fontSize = 10.5.sp,
                ),
                maxLines = 1,
            )
        }
        Spacer(GlanceModifier.width(1.dp).height(1.dp))
        Row {
            Image(
                provider = ImageProvider(
                    R.drawable.schedule_app_ic_fluent_people_16_regular,
                ),
                contentDescription = null,
                modifier = GlanceModifier.size(12.dp),
            )
            Spacer(GlanceModifier.width(3.dp).height(1.dp))
            Text(
                text = lesson.groups.joinToString { it.title },
                style = TextStyle(
                    fontSize = 10.5.sp,
                ),
                maxLines = 1,
            )
        }
        Spacer(GlanceModifier.width(1.dp).height(1.dp))
        Row {
            Image(
                provider = ImageProvider(
                    R.drawable.schedule_app_ic_fluent_location_12_regular,
                ),
                contentDescription = null,
                modifier = GlanceModifier.size(12.dp),
            )
            Spacer(GlanceModifier.width(3.dp).height(1.dp))
            Text(
                text = lesson.places.joinToString { it.title },
                style = TextStyle(
                    fontSize = 10.5.sp,
                ),
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun LessonSelector(
    currentIndex: Int,
    lessonsCount: Int,
    modifier: GlanceModifier = GlanceModifier,
) {
    Row(modifier) {
        Image(
            provider = ImageProvider(
                R.drawable.schedule_app_ic_fluent_arrow_circle_left_16_regular,
            ),
            contentDescription = null,
            modifier = GlanceModifier
                .size(16.dp)
                .clickable(
                    actionRunCallback<UpdateLessonIndexActionCallback>(
                        parameters = actionParametersOf(
                            lessonIndexParamKey to -1,
                        ),
                    ),
                ),
        )
        Text(
            text = "${currentIndex + 1}/$lessonsCount",
            maxLines = 1,
            style = TextStyle(
                fontSize = 10.sp,
            ),
            modifier = GlanceModifier.padding(horizontal = 3.dp),
        )
        Image(
            provider = ImageProvider(
                R.drawable.schedule_app_ic_fluent_arrow_circle_right_16_regular,
            ),
            contentDescription = null,
            modifier = GlanceModifier
                .size(16.dp)
                .clickable(
                    actionRunCallback<UpdateLessonIndexActionCallback>(
                        parameters = actionParametersOf(
                            lessonIndexParamKey to 1,
                        ),
                    ),
                ),
        )
    }
}

private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM, HH:mm")

@Composable
private fun Timestamp(
    updateDateTime: ZonedDateTime,
    modifier: GlanceModifier = GlanceModifier,
) {
    Row(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
    ) {
        Text(
            text = updateDateTime.format(dateTimeFormatter),
            maxLines = 1,
            style = TextStyle(
                fontSize = 10.sp,
            ),
        )
        Spacer(GlanceModifier.width(3.dp).height(1.dp))
        Image(
            provider = ImageProvider(
                R.drawable.schedule_app_ic_fluent_arrow_clockwise_16_regular,
            ),
            contentDescription = null,
            modifier = GlanceModifier
                .size(16.dp)
                .clickable(actionRunCallback<UpdateActionCallback>()),
        )
    }
}
