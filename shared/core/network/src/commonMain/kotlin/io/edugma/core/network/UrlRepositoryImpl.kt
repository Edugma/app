package io.edugma.core.network

import io.edugma.core.api.api.Path
import io.edugma.core.api.repository.SettingsRepository
import io.edugma.core.api.repository.UrlRepository
import io.edugma.core.api.repository.UrlTemplateRepository
import io.edugma.core.api.repository.get
import io.edugma.core.api.repository.save

class UrlRepositoryImpl(
    private val settingsRepository: SettingsRepository,
) : UrlRepository, UrlTemplateRepository {

    private var templates: Map<String, Path>? = emptyMap()
    private var predefinedVariables: Map<String, String> = emptyMap()

    override suspend fun init() {
        templates = settingsRepository.get<Map<String, Path>>(URL_TEMPLATES_KEY)
        // TODO Remove this
        templates = edugmaApi.endpoints
        predefinedVariables = edugmaApi.variables
    }

    override suspend fun setTemplates(templates: Map<String, Path>) {
        settingsRepository.save(URL_TEMPLATES_KEY, templates)
        this.templates = templates
    }

    override fun url(name: String, params: Map<String, String>): String {
        val templates = checkNotNull(templates) { "Templates are null" }
        val template = checkNotNull(templates[name]) { "Url $name not found" }.url
        val resultParams = predefinedVariables + params

        return replaceParams(template, resultParams)
    }

    override fun queryParams(name: String, params: Map<String, String>): Map<String, String> {
        val queryParams = checkNotNull(templates?.get(name)).queryParams ?: return emptyMap()
        val resultParams = predefinedVariables + params

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
        val headerParams = checkNotNull(templates?.get(name)).headerParams ?: return emptyMap()
        val resultParams = predefinedVariables + params

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
        private const val URL_TEMPLATES_KEY = "url_templates_key"
    }
}

private val edugmaApi = EdugmaApi(
    variables = mapOf(
        "baseUrl" to "http://devspare.mospolytech.ru:8003",
    ),
    endpoints = mapOf(
        "schedule-compact-get" to Path(
            url = "{baseUrl}/schedules/compact/{type}/{key}",
        ),
        "schedule-compact-complex-post" to Path(
            url = "{baseUrl}/schedules/compact/complex",
        ),
        "schedule-sources-get" to Path(
            url = "{baseUrl}/schedule/sources/{type}",
            queryParams = mapOf(
                "query" to "{query}",
                "page" to "{page}",
                "limit" to "{limit}",
            ),
        ),
        "schedule-sources-types-get" to Path(
            url = "{baseUrl}/schedule/sources",
        ),
        "schedule-info-group-get" to Path(
            url = "{baseUrl}/schedule/group/{id}",
        ),
        "schedule-info-teacher-get" to Path(
            url = "{baseUrl}/schedule/teacher/{id}",
        ),
        "schedule-info-place-get" to Path(
            url = "{baseUrl}/schedule/place/{id}",
        ),
        "schedule-info-subject-get" to Path(
            url = "{baseUrl}/schedule/subject/{id}",
        ),
        "schedule-info-lesson-type-get" to Path(
            url = "{baseUrl}/schedule/lesson-type/{id}",
        ),
        "schedule-places-free-post" to Path(
            url = "{baseUrl}/schedule/places/free",
        ),
        "schedule-places-occupancy-get" to Path(
            url = "{baseUrl}/schedule/places/occupancy/{placeId}",
        ),

        "account-login-post" to Path(
            url = "{baseUrl}/login",
        ),
        "account-lk-token-get" to Path(
            url = "{baseUrl}/getLkToken",
        ),
        "account-peoples-classmates-get" to Path(
            url = "{baseUrl}/peoples/classmates",
        ),
        "account-peoples-students-get" to Path(
            url = "{baseUrl}/peoples/students",
            queryParams = mapOf(
                "query" to "{query}",
                "page" to "{page}",
                "limit" to "{limit}",
            ),
        ),
        "account-peoples-teachers-get" to Path(
            url = "{baseUrl}/peoples/teachers",
            queryParams = mapOf(
                "query" to "{query}",
                "page" to "{page}",
                "limit" to "{limit}",
            ),
        ),
        "account-applications-get" to Path(
            url = "{baseUrl}/applications",
        ),
        "account-performance-get" to Path(
            url = "{baseUrl}/performance/{periodId}",
        ),
        "account-personal-get" to Path(
            url = "{baseUrl}/personal",
        ),
        "account-payments-get" to Path(
            url = "{baseUrl}/payments",
        ),
    ),
)

class EdugmaApi(
    val variables: Map<String, String>,
    val endpoints: Map<String, Path>,
)
