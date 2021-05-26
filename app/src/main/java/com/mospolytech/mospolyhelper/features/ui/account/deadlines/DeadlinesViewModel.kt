package com.mospolytech.mospolyhelper.features.ui.account.deadlines

import androidx.lifecycle.MutableLiveData
import com.mospolytech.mospolyhelper.domain.account.deadlines.model.Deadline
import com.mospolytech.mospolyhelper.domain.account.deadlines.usecase.DeadlinesUseCase
import com.mospolytech.mospolyhelper.features.ui.common.Mediator
import com.mospolytech.mospolyhelper.features.ui.common.ViewModelBase
import com.mospolytech.mospolyhelper.features.ui.common.ViewModelMessage
import com.mospolytech.mospolyhelper.utils.Result2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import org.koin.core.component.KoinComponent

class DeadlinesViewModel(
    mediator: Mediator<String, ViewModelMessage>,
    private val useCase: DeadlinesUseCase
) : ViewModelBase(mediator, DeadlinesViewModel::class.java.simpleName), KoinComponent {

    val deadlines = MutableStateFlow<Result2<List<Deadline>>>(Result2.loading())

    val deadline: MutableLiveData<Deadline> by lazy {
        MutableLiveData()
    }

    suspend fun downloadInfo() {
        useCase.getInfo().collect {
            deadlines.value = it
        }
    }

    suspend fun getInfo() {
        useCase.getLocalInfo().collect {
            deadlines.value = it
        }
        useCase.getInfo().collect {
            deadlines.value = it
        }
    }

    suspend fun setInfo(deadlinesList: List<Deadline>) {
        useCase.setInfo(deadlinesList).collect {
            deadlines.value = it
        }
    }
}