package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Exit(
    @SerialName("position")
    val position: Position,
    @SerialName("width")
    val width: Int,
)
