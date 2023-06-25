package at.ac.tuwien.foop.common.models.domain.socket

sealed interface AoopMessage

sealed interface GlobalMessage : AoopMessage {
    data class StateUpdate(
        val map: GameBoard,
        val status: GameState,
    ) : GlobalMessage
}

sealed interface PrivateMessage : AoopMessage {
    data class JoinRequest(
        val id: String,
    ) : PrivateMessage

    data class MoveCommand(
        val id: String,
        val direction: Direction,
        val type: MoveCommandType,
    ) : PrivateMessage
}
