package at.ac.tuwien.foop.common.models.dtos.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The available directions for a key press
 */
@Serializable
enum class DirectionDto {
    @SerialName("UP")
    UP,

    @SerialName("DOWN")
    DOWN,

    @SerialName("LEFT")
    LEFT,

    @SerialName("RIGHT")
    RIGHT,
}
