package at.ac.tuwien.foop.common.models.dtos.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface AoopMessageDto

/**
 * Messages send globally between the server and all players
 * */
@Serializable
@SerialName("global")
sealed interface GlobalMessageDto : AoopMessageDto {
    /**
     * Update about the state, containing the state of all players (cats), mice and the current status of the game
     * */
    @Serializable
    @SerialName("global_state_update")
    data class StateUpdateDto(
        @SerialName("map")
        val map: GameBoardDto,
        @SerialName("state")
        val state: GameStatusDto,
    ) : GlobalMessageDto
}

/**
 * Messages sent between the server and a single player
 * */
@Serializable
@SerialName("private")
sealed interface PrivateMessageDto : AoopMessageDto {
    /**
     * Message sent by the player to the server to join itself as a player
     * */
    @Serializable
    @SerialName("private_join_request")
    data class JoinRequestDto(
        @SerialName("id")
        val id: String,
    ) : PrivateMessageDto

    /**
     * Message sent to the server when a keypress has occurred
     * */
    @Serializable
    @SerialName("private_move_command")
    data class MoveCommandDto(
        @SerialName("id")
        val id: String,
        @SerialName("direction")
        val direction: DirectionDto,
        @SerialName("move_type")
        val moveType: MoveCommandTypeDto,
    ) : PrivateMessageDto
}
