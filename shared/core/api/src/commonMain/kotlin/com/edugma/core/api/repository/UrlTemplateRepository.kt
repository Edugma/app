package com.edugma.core.api.repository

import com.edugma.core.api.model.NodeState

interface UrlTemplateRepository {
    suspend fun init(): NodeState
}
