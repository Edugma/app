package com.edugma.features.account.personal

sealed interface PersonalAction {
    data object OnRefresh : PersonalAction
}
