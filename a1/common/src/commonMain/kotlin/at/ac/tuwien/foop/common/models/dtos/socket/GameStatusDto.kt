package at.ac.tuwien.foop.common.models.dtos.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Enum describing the current state if the game
 * */
@Serializable
enum class GameStatusDto {
    @SerialName("WAITING")
    WAITING,

    @SerialName("RUNNING")
    RUNNING,

    @SerialName("GAME_OVER")
    GAME_OVER
}
