package at.ac.tuwien.foop.common.models.dtos.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Object representing the position of something on the map relative to the top left corner (0,0)
 * */
@Serializable
data class PositionDto(
    @SerialName("x")
    var x: Long,
    @SerialName("y")
    var y: Long,
)
