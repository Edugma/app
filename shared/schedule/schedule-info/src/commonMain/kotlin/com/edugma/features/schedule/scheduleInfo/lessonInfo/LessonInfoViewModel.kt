package com.edugma.features.schedule.scheduleInfo.lessonInfo

import com.edugma.core.api.model.Coordinates
import com.edugma.core.api.utils.isUrl
import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic2
import com.edugma.core.navigation.core.router.external.ExternalRouter
import com.edugma.core.navigation.schedule.ScheduleInfoScreens
import com.edugma.features.schedule.domain.model.compact.CompactPlaceInfo
import com.edugma.features.schedule.domain.model.lesson.LessonEvent
import com.edugma.features.schedule.domain.model.place.PlaceType
import com.edugma.features.schedule.domain.usecase.ScheduleUseCase
import kotlinx.datetime.LocalDate

class LessonInfoViewModel(
    private val scheduleUseCase: ScheduleUseCase,
    private val externalRouter: ExternalRouter,
) : FeatureLogic2<LessonInfoState>() {
    override fun initialState(): LessonInfoState {
        return LessonInfoState()
    }

    init {
//        launchCoroutine {
//            stateFlow.prop { lessonInfo }
//                .collectLatest {
//                    val teachers = it?.lesson?.teachers?.map {
//                        scheduleUseCase.getTeacher(it.id).first()
//                    }?.filterNotNull() ?: emptyList()
//
//                    newState {
//                        copy(
//                            teachers = teachers,
//                        )
//                    }
//                }
//        }
    }

    fun onLessonInfo(
        eventId: String,
        currentDate: LocalDate,
    ) {
        launchCoroutine {
            val lessonEvent = scheduleUseCase.getEvent(eventId)

            newState {
                copy(
                    lessonInfo = lessonEvent,
                    date = currentDate,
                )
            }

            // TODO
            val firstBuilding = lessonEvent?.places?.firstOrNull { it.type == PlaceType.Building }
            if (firstBuilding != null) {
                val place = scheduleUseCase.getLocalPlaceInfo(firstBuilding.id)
                val coordinates = (place as? CompactPlaceInfo.Building)?.coordinates

                if (coordinates != null) {
                    newState {
                        copy(
                            coordinates = coordinates
                        )
                    }
                }
            }
        }
    }

    fun onTeacherClick(id: String) {
        scheduleRouter.navigateTo(ScheduleInfoScreens.TeacherInfo(id))
    }

    fun onGroupClick(id: String) {
        scheduleRouter.navigateTo(ScheduleInfoScreens.GroupInfo(id))
    }

    fun onPlaceClick(id: String) {
        val description = state.lessonInfo?.places?.firstOrNull { it.id == id }?.description
        if (description != null && isUrl(description)) {
            externalRouter.openUri(description)
        } else {
            scheduleRouter.navigateTo(ScheduleInfoScreens.PlaceInfo(id))
        }
    }

    fun exit() {
        scheduleRouter.back()
    }
}

data class LessonInfoState(
    val lessonInfo: LessonEvent? = null,
    val coordinates: Coordinates? = null,
    val date: LocalDate? = null,
)
