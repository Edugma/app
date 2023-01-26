package io.edugma.data.nodes.model

import io.edugma.domain.nodes.model.NodeContract
import kotlinx.serialization.Serializable
import org.kodein.db.model.orm.Metadata

@Serializable
data class NodeContractDao(
    override val id: String,
    val data: NodeContract?,
) : Metadata {
    companion object {
        fun from(url: String, nodeContract: NodeContract?) =
            NodeContractDao(url, nodeContract)
    }

    fun toModel() = data
}
