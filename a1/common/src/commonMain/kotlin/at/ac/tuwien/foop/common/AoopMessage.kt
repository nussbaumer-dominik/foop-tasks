package at.ac.tuwien.foop.common

import at.ac.tuwien.foop.common.domain.Direction
import at.ac.tuwien.foop.common.domain.GameBoardDto
import at.ac.tuwien.foop.common.domain.GameState
import at.ac.tuwien.foop.common.domain.PlayerDto
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
        val map: GameBoardDto,
    ) : GlobalMessage

    /**
     * Update about the state, containing the state of all players (cats), mice and the current status of the game
     * */
    @Serializable
    @SerialName("global_state_update")
    data class StateUpdate(
        @SerialName("map")
        val map: GameBoardDto,
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
        @SerialName("playerDto")
        val playerDto: PlayerDto,
    ) : PrivateMessage

    /**
     * Message sent to the server when a keypress has occurred
     * */
    @Serializable
    @SerialName("private_move_command")
    data class MoveCommand(
        @SerialName("playerDto")
        val playerDto: PlayerDto? = null,
        @SerialName("direction")
        val direction: Direction,
        @SerialName("move_command_type")
        val type: MoveCommandType,
    ) : PrivateMessage

    enum class MoveCommandType {
        MOVE, STOP
    }
}
