package io.edugma.core.api.repository

import io.edugma.core.api.model.NodeState

interface UrlTemplateRepository {
    suspend fun init(): NodeState
}
