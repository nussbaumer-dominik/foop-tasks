package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Mouse(
    @SerialName("id")
    val id: Long,
    @SerialName("position")
    val position: Position,
)
