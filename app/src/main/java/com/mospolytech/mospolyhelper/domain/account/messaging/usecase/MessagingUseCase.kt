package com.mospolytech.mospolyhelper.domain.account.messaging.usecase

import com.mospolytech.mospolyhelper.domain.account.messaging.model.Message
import com.mospolytech.mospolyhelper.domain.account.messaging.repository.MessagingRepository
import com.mospolytech.mospolyhelper.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class MessagingUseCase(
    private val repository: MessagingRepository
) {
    suspend fun getDialog(dialogKey: String): Flow<Result<List<Message>>> =
        repository.getDialog(dialogKey).onStart {
            emit(Result.loading())
        }
    suspend fun getLocalDialog(dialogKey: String): Flow<Result<List<Message>>> =
        repository.getLocalDialog(dialogKey).onStart {
            //emit(Result.loading())
        }

    suspend fun sendMessage(dialogKey: String, message: String, fileNames: List<String>): Flow<Result<List<Message>>> =
        repository.sendMessage(dialogKey, message, fileNames).onStart {
            emit(Result.loading())
        }

    fun getName(): String {
        val name = repository.getName().substringBeforeLast(" ", "")
        return "${name.substringAfter(" ")} ${name.substringBefore(" ")}"
    }

    fun getAvatar(): String {
        var avatar = repository.getAvatar().replace("https://e.mospolytech.ru/img/", "")
        avatar = avatar.replace("photos/", "")
        return avatar
    }
}