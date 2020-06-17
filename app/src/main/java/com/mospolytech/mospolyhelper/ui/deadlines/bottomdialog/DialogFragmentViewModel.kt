package com.mospolytech.mospolyhelper.ui.deadlines.bottomdialog

import android.content.Context
import com.mospolytech.mospolyhelper.repository.dao.ScheduleDao
import com.mospolytech.mospolyhelper.repository.local.AppDatabase
import com.mospolytech.mospolyhelper.repository.local.DeadlinesRepository
import com.mospolytech.mospolyhelper.repository.models.Deadline
import com.mospolytech.mospolyhelper.ui.common.Mediator
import com.mospolytech.mospolyhelper.ui.common.ViewModelBase
import com.mospolytech.mospolyhelper.utils.ContextProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DialogFragmentViewModel/*(app: Application)*/ :
    /*AndroidViewModel(app) {*/
    ViewModelBase(Mediator(), DialogFragmentViewModel::class.java.simpleName) {

    companion object {
        const val DeadlineAdd = "DeadlinesAdd"
    }

    private val groupTitle = "181-721"
    private val dao = ScheduleDao()
    private val database: AppDatabase = AppDatabase.getDatabase(ContextProvider.context as Context)
    private lateinit var deadlinesRepository: DeadlinesRepository

    fun newRepository() {
        deadlinesRepository =
            DeadlinesRepository(
                database
            )
    }

    fun saveInformation(deadline: Deadline) {
        deadlinesRepository.insertDeadline(deadline)
    }

    fun updateOne(deadline: Deadline) {
        deadlinesRepository.updateDeadline(deadline)
    }

    override fun onCleared() {
        super.onCleared()
        deadlinesRepository.cancel()
    }

    fun setUpSchedule() =
        GlobalScope.launch(Dispatchers.Main) {
            val schedule = if (groupTitle.isEmpty()) {
                null
            } else {
                dao.getSchedule(groupTitle, false, false)
                    ?: dao.getSchedule(groupTitle, false, true)
            }
            dao.allDataFromSchedule(schedule!!)
        }
}