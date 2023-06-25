package helper

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.*
import at.ac.tuwien.foop.common.client.A1SocketClient
import at.ac.tuwien.foop.common.models.domain.socket.Direction
import at.ac.tuwien.foop.common.models.domain.socket.MoveCommandType
import at.ac.tuwien.foop.common.models.domain.socket.PrivateMessage
import game.A1Game

// TODO: add Map to store currently pressed keys to avoid unnecessary network traffic

suspend fun KeyEvent.handleKeyEvent(
    game: A1Game,
    socketClient: A1SocketClient,
) {
    val type = when (type) {
        KeyEventType.KeyDown -> MoveCommandType.MOVE
        KeyEventType.KeyUp -> MoveCommandType.STOP
        else -> return
    }

    val direction = key.toDirection()
    if (direction != null) {
        val currentPlayer = game.getCurrentPlayer()
        if (currentPlayer != null) {
            socketClient.move(
                PrivateMessage.MoveCommand(
                    id = currentPlayer.id,
                    direction = direction,
                    type = type,
                )
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun Key.toDirection(): Direction? {
    return when (this) {
        Key.DirectionUp -> Direction.UP
        Key.DirectionDown -> Direction.DOWN
        Key.DirectionLeft -> Direction.LEFT
        Key.DirectionRight -> Direction.RIGHT
        else -> null
    }
}
