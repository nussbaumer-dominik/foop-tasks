package models

import at.ac.tuwien.foop.common.models.domain.socket.GameBoard
import at.ac.tuwien.foop.common.models.domain.socket.GameStatus

data class GameState(
    val gameBoard: GameBoard,
    val gameStatus: GameStatus,
)
