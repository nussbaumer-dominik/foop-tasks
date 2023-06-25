package game

import at.ac.tuwien.foop.common.models.domain.socket.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import models.GameState

interface A1Game {
    fun getCurrentPlayer(): Player?
    fun getCurrentPlayerId(): String?
    fun observeCurrentPlayer(): Flow<Player?>
    fun updateCurrentPlayerId(id: String)

    fun observeGameState(): StateFlow<GameState?>
    fun updateGameState(gameState: GameState)
}
