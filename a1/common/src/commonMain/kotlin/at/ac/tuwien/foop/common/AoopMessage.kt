package at.ac.tuwien.foop.common

import at.ac.tuwien.foop.common.domain.*
import at.ac.tuwien.foop.common.domain.GameBoard
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface AoopMessage

/**
 * Messages send globally between the server and all players
 * */
@Serializable
@SerialName("global")
sealed interface GlobalMessage : AoopMessage {
    /**
     * Update the clients about the structure of the map of the current game
     * */
    @Serializable
    @SerialName("global_map_update")
    data class MapUpdate(
            @SerialName("map")
        val map: GameBoard,
    ) : GlobalMessage

    /**
     * Update about the state, containing the state of all players (cats), mice and the current status of the game
     * */
    @Serializable
    @SerialName("global_state_update")
    data class StateUpdate(
        @SerialName("players")
        val players: Set<Player>,
        @SerialName("mice")
        val mice: Set<Mouse>,
        @SerialName("state")
        val state: GameState,
    ) : GlobalMessage
}

/**
 * Messages sent between the server and a single player
 * */
@Serializable
@SerialName("private")
sealed interface PrivateMessage : AoopMessage {
    /**
     * Message sent to the player when they join the game with generic information about itself
     * */
    @Serializable
    @SerialName("private_setup_info")
    data class SetupInfo(
        @SerialName("player")
        val player: Player,
    ) : PrivateMessage

    /**
     * Message sent to the server when the position of the player changes because of play movement
     * */
    @Serializable
    @SerialName("private_position_update")
    data class PositionUpdate(
        @SerialName("position")
        val position: Position,
    ) : PrivateMessage
}
