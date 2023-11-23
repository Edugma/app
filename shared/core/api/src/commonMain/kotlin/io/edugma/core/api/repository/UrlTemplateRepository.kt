package io.edugma.core.api.repository

import io.edugma.core.api.api.Path

interface UrlTemplateRepository {
    suspend fun init()

    suspend fun setTemplates(templates: Map<String, Path>)
}
