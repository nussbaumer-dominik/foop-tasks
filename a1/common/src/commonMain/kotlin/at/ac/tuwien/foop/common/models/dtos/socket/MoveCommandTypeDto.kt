package at.ac.tuwien.foop.common.models.dtos.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The kind of move which is sent to the server
 */
@Serializable
enum class MoveCommandTypeDto {
    @SerialName("MOVE")
    MOVE,

    @SerialName("STOP")
    STOP
}
