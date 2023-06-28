package at.ac.tuwien.foop.common.models.dtos.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SizeDto(
    @SerialName("width")
    val width: Int,
    @SerialName("height")
    val height: Int
)
