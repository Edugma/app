package io.edugma.domain.base.model

import kotlinx.serialization.Serializable

@Serializable
data class PagingDTO<T>(
    val count: Int,
    val previousPage: Int? = null,
    val nextPage: Int? = null,
    val data: List<T>,
)
