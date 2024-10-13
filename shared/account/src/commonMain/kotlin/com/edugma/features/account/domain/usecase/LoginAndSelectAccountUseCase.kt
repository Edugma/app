package com.edugma.features.account.domain.usecase

import com.edugma.features.account.data.repository.AccountRepositoryImpl
import com.edugma.features.account.domain.repository.AuthorizationRepository

class LoginAndSelectAccountUseCase(
    private val authorizationRepository: AuthorizationRepository,
    private val accountRepository: AccountRepositoryImpl,
) {
    suspend operator fun invoke(
        login: String,
        password: String,
    ) {
        val token = authorizationRepository.authorize(login, password)
        authorizationRepository.setCurrentToken(
            accessToken = token.accessToken,
            refreshToken = token.refreshToken,
        )
        accountRepository.clearSelectedAccount()
        val newAccountGroupId = accountRepository.createNewAccountGroupFromToken(
            accessToken = token.accessToken,
            refreshToken = token.refreshToken,
        )
        accountRepository.selectAccountGroup(newAccountGroupId)
        authorizationRepository.clearAccountCache()
    }
}
