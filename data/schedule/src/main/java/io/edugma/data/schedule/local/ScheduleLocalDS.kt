package io.edugma.data.schedule.local

import io.edugma.data.schedule.model.ScheduleDao
import io.edugma.domain.schedule.model.source.ScheduleSource
import org.kodein.db.DB
import org.kodein.db.asModelSequence
import org.kodein.db.find

class ScheduleLocalDS(
    private val db: DB
) {
    fun getLast(source: ScheduleSource) =
        getLast(source.id)

    fun getLast(id: String): ScheduleDao? {
        val lastScheduleDao = db.find<ScheduleDao>().byId(id).use {
            it.asModelSequence().maxByOrNull { it.date }
        }
        return lastScheduleDao
    }

    fun getAll(source: ScheduleSource) =
        getAll(source.id)

    fun getAll(id: String): List<ScheduleDao> {
        return db.find<ScheduleDao>().byId(id).use {
            it.asModelSequence().toList()
        }
    }

    fun add(dao: ScheduleDao) {
        val lastScheduleDao = getLast(dao.id)

        if (lastScheduleDao?.days != dao.days) {
            db.put(dao)
        }
    }
}