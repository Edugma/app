package io.edugma.features.account.domain.repository

import io.edugma.core.api.utils.LceFlow
import io.edugma.features.account.domain.model.Personal

interface PersonalRepository {
    fun getPersonalInfo(forceUpdate: Boolean = false): LceFlow<Personal>
}
