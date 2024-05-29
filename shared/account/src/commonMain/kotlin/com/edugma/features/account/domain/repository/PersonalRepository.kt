package com.edugma.features.account.domain.repository

import com.edugma.core.api.utils.LceFlow
import com.edugma.features.account.domain.model.Personal

interface PersonalRepository {
    fun getPersonalInfo(forceUpdate: Boolean = false): LceFlow<Personal>
}
