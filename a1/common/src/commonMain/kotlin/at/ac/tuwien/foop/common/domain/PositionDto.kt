package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Object representing the position of something on the map relative to the top left corner (0,0)
 * */
@Serializable
data class PositionDto(
    @SerialName("x")
    var x: Int,
    @SerialName("y")
    var y: Int,
)
