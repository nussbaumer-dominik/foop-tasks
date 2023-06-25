package models

import at.ac.tuwien.foop.common.models.domain.socket.GameBoard
import at.ac.tuwien.foop.common.models.domain.socket.GameState

data class GameState(
    val gameBoard: GameBoard,
    val gameState: GameState,
)
