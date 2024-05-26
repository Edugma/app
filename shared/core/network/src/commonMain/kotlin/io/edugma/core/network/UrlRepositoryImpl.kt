package io.edugma.core.network

import io.edugma.core.api.api.EdugmaApi
import io.edugma.core.api.api.Node
import io.edugma.core.api.model.NodeState
import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.NodesRepository
import io.edugma.core.api.repository.SettingsRepository
import io.edugma.core.api.repository.UrlRepository
import io.edugma.core.api.repository.UrlTemplateRepository
import io.edugma.core.api.repository.get
import io.edugma.core.api.repository.getFlow
import io.edugma.core.api.repository.save

class UrlRepositoryImpl(
    private val settingsRepository: SettingsRepository,
    private val cacheRepository: CacheRepository,
    private val nodesRepository: NodesRepository,
) : UrlRepository, UrlTemplateRepository {

    private var edugmaApi: EdugmaApi? = null

    override suspend fun init(): NodeState {
        edugmaApi = nodesRepository.getSelectedContract()
        if (edugmaApi == null) {
            return NodeState.Selection
        } else {
            return NodeState.Ready
        }
    }

    override fun url(name: String, params: Map<String, String>): String {
        val edugmaApi = checkNotNull(edugmaApi) { "EdugmaApi are null" }

        val template = checkNotNull(edugmaApi.endpoints[name]) { "Url $name not found" }.url
        val resultParams = edugmaApi.variables + params

        return replaceParams(template, resultParams)
    }

    override fun queryParams(name: String, params: Map<String, String>): Map<String, String> {
        val edugmaApi = checkNotNull(edugmaApi) { "EdugmaApi are null" }

        val queryParams = checkNotNull(edugmaApi.endpoints.get(name)).queryParams ?: return emptyMap()
        val resultParams = edugmaApi.variables + params

        return queryParams.mapNotNull { (name, template) ->
            val value = replaceParams(template, resultParams)
            if (value.isEmpty()) {
                null
            } else {
                name to value
            }
        }.toMap()
    }

    override fun headerParams(name: String, params: Map<String, String>): Map<String, String> {
        val edugmaApi = checkNotNull(edugmaApi) { "EdugmaApi are null" }

        val headerParams = checkNotNull(edugmaApi.endpoints.get(name)).headerParams ?: return emptyMap()
        val resultParams = edugmaApi.variables + params

        return headerParams.mapNotNull { (name, template) ->
            val value = replaceParams(template, resultParams)
            if (value.isEmpty()) {
                null
            } else {
                name to value
            }
        }.toMap()
    }

    private fun replaceParams(
        template: String,
        params: Map<String, String>,
    ): String {
        var i = 0
        val resultString = buildString {
            while (i < template.length) {
                if (template.isControlChar(i, PARAMETER_START_CHAR)) {
                    i = appendParameter(
                        template = template,
                        startIndex = i,
                        paramMap = params,
                    )
                } else {
                    append(template[i])
                    i++
                }
            }
        }

        return resultString
    }

    /**
     * @return Next index after parameter
     */
    private fun StringBuilder.appendParameter(
        template: String,
        startIndex: Int,
        paramMap: Map<String, String>,
    ): Int {
        var i = startIndex + 1
        while (i < template.length) {
            if (template.isControlChar(i, PARAMETER_END_CHAR)) {
                break
            } else {
                i++
            }
        }
        val lastIndex = i
        check(startIndex + 1 != lastIndex) {
            "Parameter is not valid. From $startIndex to $lastIndex"
        }
        val paramName = template.substring(startIndex + 1, lastIndex)
        val paramValue = paramMap[paramName]
        if (paramValue != null) {
            append(paramValue)
        }
        return lastIndex + 1
    }

    private fun String.isControlChar(index: Int, controlChar: Char): Boolean {
        val currentChar = get(index)
        return currentChar == controlChar &&
            (index == 0 || get(index - 1) != PARAMETER_ESCAPE_CHAR)
    }

    companion object {
        private const val PARAMETER_START_CHAR = '{'
        private const val PARAMETER_END_CHAR = '}'
        private const val PARAMETER_ESCAPE_CHAR = '\\'
        private const val URL_TEMPLATES_KEY = "edugma_api"
    }
}
