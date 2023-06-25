package helper

import at.ac.tuwien.foop.common.models.domain.socket.AoopMessage
import at.ac.tuwien.foop.common.models.domain.socket.GlobalMessage
import game.A1Game
import models.GameState

fun handleMessage(game: A1Game, message: AoopMessage) = when (message) {
    is GlobalMessage.StateUpdate -> game.updateGameState(
        GameState(
            gameBoard = message.map,
            gameState = message.status,
        )
    )

    else -> println("Receive not known or uninteresting socket message! $message")
}
