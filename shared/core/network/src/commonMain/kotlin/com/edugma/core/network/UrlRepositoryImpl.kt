package com.edugma.core.network

import com.edugma.core.api.api.CrashAnalytics
import com.edugma.core.api.api.EdugmaApi
import com.edugma.core.api.model.NodeState
import com.edugma.core.api.repository.CacheRepository
import com.edugma.core.api.repository.NodesRepository
import com.edugma.core.api.repository.SettingsRepository
import com.edugma.core.api.repository.UrlRepository
import com.edugma.core.api.repository.UrlTemplateRepository
import com.edugma.core.api.repository.get
import com.edugma.core.api.utils.IO
import com.edugma.core.api.utils.onResult
import io.ktor.http.HttpMethod
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch

class UrlRepositoryImpl(
    private val settingsRepository: SettingsRepository,
    private val cacheRepository: CacheRepository,
    private val nodesRepository: NodesRepository,
) : UrlRepository, UrlTemplateRepository {

    private val flag = MutableStateFlow<Boolean>(false)
    private val _edugmaApi = MutableStateFlow<EdugmaApi?>(null)
    private val edugmaApi: EdugmaApi? get() = _edugmaApi.value

    // TODO делать все скоупы только через фабрики, чтобы мы собирали их одинаково
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override suspend fun init(): NodeState {
        scope.launch(
            CoroutineExceptionHandler { coroutineContext, throwable ->
                CrashAnalytics.logException(throwable)
            }
        ) {
            val selectedContractLce = nodesRepository.getSelectedContract()

            if (selectedContractLce == null) {
                flag.value = true
            } else {
                selectedContractLce.onResult(
                    onSuccess = {
                        _edugmaApi.value = it.valueOrThrow
                        flag.value = true
                    },
                    onFailure = {
                        CrashAnalytics.logException(it.exceptionOrThrow)
                        flag.value = true
                    }
                )
            }
        }
        flag.takeWhile { !it }.collect()

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

    override fun getMethod(name: String): HttpMethod {
        val edugmaApi = checkNotNull(edugmaApi) { "EdugmaApi are null" }

        val method = checkNotNull(edugmaApi.endpoints.get(name)).method

        return when (method) {
            "get" -> HttpMethod.Get
            "post" -> HttpMethod.Post
            "put" -> HttpMethod.Put
            "patch" -> HttpMethod.Patch
            "delete" -> HttpMethod.Delete
            "head" -> HttpMethod.Head
            "options" -> HttpMethod.Options
            else -> error("Unsupported http method")
        }
    }

    override fun isSecure(name: String): Boolean {
        val edugmaApi = checkNotNull(edugmaApi) { "EdugmaApi are null" }

        return checkNotNull(edugmaApi.endpoints.get(name)).security
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
