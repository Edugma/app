package io.edugma.data.schedule.local

import io.edugma.data.schedule.model.ScheduleDao
import io.edugma.data.schedule.model.ScheduleSourceFullDao
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.model.source.ScheduleSource
import io.edugma.domain.schedule.model.source.ScheduleSourceFull
import org.kodein.db.*

class ScheduleLocalDS(
    private val db: DB
) {
    fun saveSchedule(source: ScheduleSource, schedule: List<ScheduleDay>?) {
        db.put(
            ScheduleDao.from(source, schedule)
        )
    }

    fun getSchedule(source: ScheduleSource): Result<List<ScheduleDay>> {
        return runCatching {
            //val key = db.keyFrom(source.id)
            db.getById<ScheduleDao>(source.id)?.days ?: emptyList()
        }
    }

    fun setSelectedSource(source: ScheduleSourceFull) {
        db.put(
            ScheduleSourceFullDao.from(source, "Selected")
        )
    }

    fun getSelectedSource(): Result<ScheduleSourceFull?> {
        return runCatching {
            val a = db.flowOf(db.keyById<ScheduleSourceFullDao>("Selected"))
            db.getById<ScheduleSourceFullDao>("Selected")?.source
        }
    }
}