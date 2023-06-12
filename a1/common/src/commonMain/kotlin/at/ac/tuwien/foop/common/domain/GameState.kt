package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Enum describing the current state if the game
 * */
@Serializable
enum class GameState {
    @SerialName("WAITING")
    WAITING,

    @SerialName("RUNNING")
    RUNNING,

    @SerialName("MICE_WON")
    MICE_WON,

    @SerialName("CATS_WON")
    CATS_WON,
}
