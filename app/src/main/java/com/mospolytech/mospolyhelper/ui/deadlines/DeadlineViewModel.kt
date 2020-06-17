package com.mospolytech.mospolyhelper.ui.deadlines

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.mospolytech.mospolyhelper.repository.local.AppDatabase
import com.mospolytech.mospolyhelper.repository.local.DeadlinesRepository
import com.mospolytech.mospolyhelper.repository.models.Deadline
import com.mospolytech.mospolyhelper.ui.common.Mediator
import com.mospolytech.mospolyhelper.ui.common.ViewModelBase
import com.mospolytech.mospolyhelper.utils.ContextProvider


class DeadlineViewModel/*(app: Application)*/ :
    ViewModelBase(Mediator(), DeadlineViewModel::class.java.simpleName) {
    //AndroidViewModel(app) {
    companion object {
        const val DeadlineInfo = "DeadlinesInfo"
    }
    val edit : MutableLiveData<Deadline> = MutableLiveData()
    val delete : MutableLiveData<Deadline> = MutableLiveData()
    val nameReceiver : MutableLiveData<String> = MutableLiveData()

    private var database: AppDatabase = AppDatabase.getDatabase(ContextProvider.context as Context)
    private val deadlinesRepository =
        DeadlinesRepository(
            database
        )

    val data = deadlinesRepository.getDeadlines()
    val dataCurrent = deadlinesRepository.getDeadlinesCurrent()
    val foundData =  deadlinesRepository.foundData

    fun setName(name: String) {
        nameReceiver.value = name
    }

    fun saveInformation(deadline: Deadline) {
        deadlinesRepository.insertDeadline(deadline)
    }

    fun deleteOne(deadline: Deadline) {
        deadlinesRepository.deleteDeadline(deadline)
    }

    fun setCompleted(deadline: Deadline) {
        deadline.completed = !deadline.completed
        deadlinesRepository.updateDeadline(deadline)
    }

    fun setPinned(deadline: Deadline) {
        deadline.pinned = !deadline.pinned
        deadlinesRepository.updateDeadline(deadline)
    }

    fun edit(d: Deadline) {
        edit.value = d
    }

    fun delete(d: Deadline) {
        delete.value = d
    }

    override fun onCleared() {
        super.onCleared()
        deadlinesRepository.cancel()
    }

    fun find(name: String) {
        deadlinesRepository.findItem(name)
    }

    fun clearObserveData(a: LifecycleOwner) {
        data.removeObservers(a)
    }

    fun clearObserveDataCur(a: LifecycleOwner) {
        dataCurrent.removeObservers(a)
    }

    fun clearObserveFind(a: LifecycleOwner) {
        foundData.removeObservers(a)
    }
}