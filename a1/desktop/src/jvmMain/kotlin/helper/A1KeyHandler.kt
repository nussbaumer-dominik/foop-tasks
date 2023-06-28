package helper

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.*
import at.ac.tuwien.foop.common.client.A1SocketClient
import at.ac.tuwien.foop.common.models.domain.socket.Direction
import at.ac.tuwien.foop.common.models.domain.socket.MoveCommandType
import at.ac.tuwien.foop.common.models.domain.socket.PrivateMessage
import game.A1Game

class A1KeyHandler(
    private val game: A1Game,
    private val socketClient: A1SocketClient,
    private val activeKeys: MutableMap<Key, Boolean> = mutableMapOf()
) {
    suspend fun handleKeyEvent(keyEvent: KeyEvent) {
        val type = when (keyEvent.type) {
            KeyEventType.KeyDown -> MoveCommandType.MOVE
            KeyEventType.KeyUp -> MoveCommandType.STOP
            else -> return
        }

        if (activeKeys[keyEvent.key] == true && type == MoveCommandType.MOVE) {
            return
        }

        activeKeys[keyEvent.key] = type == MoveCommandType.MOVE

        val direction = keyEvent.key.toDirection()
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
