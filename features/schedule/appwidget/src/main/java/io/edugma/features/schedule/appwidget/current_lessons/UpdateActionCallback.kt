package io.edugma.features.schedule.appwidget.current_lessons

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback

class UpdateActionCallback : ActionCallback {
    override suspend fun onRun(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        CurrentLessonAppWidget().update(context, glanceId)
    }
}