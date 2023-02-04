package io.edugma.features.schedule.appwidget.currentLessons

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition

internal val lessonIndexPreferenceKey = intPreferencesKey("lesson-index-key")
internal val lessonIndexParamKey = ActionParameters.Key<Int>("lesson-index-key")

class UpdateLessonIndexActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        val lessonIndex = requireNotNull(parameters[lessonIndexParamKey]) {
            "Add $lessonIndexParamKey parameter in the ActionParameters of this action."
        }

        updateAppWidgetState(
            context = context,
            definition = PreferencesGlanceStateDefinition,
            glanceId = glanceId,
        ) { preferences ->
            preferences.toMutablePreferences()
                .apply {
                    this[lessonIndexPreferenceKey] =
                        (this[lessonIndexPreferenceKey] ?: 0) + lessonIndex
                }
        }

        CurrentLessonAppWidget().update(context, glanceId)
    }
}
