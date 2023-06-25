package game.impl

import at.ac.tuwien.foop.common.models.domain.socket.Player
import game.A1Game
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import models.GameState

class A1GameImpl : A1Game {
    private var currentPlayerId: String? = null
    private val gameState: MutableStateFlow<GameState?> = MutableStateFlow(null)

    override fun getCurrentPlayer(): Player? =
        gameState.value?.gameBoard?.players?.first { player -> player.id == currentPlayerId }

    override fun getCurrentPlayerId(): String? =
        currentPlayerId

    @OptIn(FlowPreview::class)
    override fun observeCurrentPlayer(): Flow<Player?> =
        gameState
            .flatMapConcat { gameState -> gameState?.gameBoard?.players?.asFlow() ?: flowOf() }
            .filter { player -> player.id == currentPlayerId }

    override fun updateCurrentPlayerId(id: String) {
        currentPlayerId = id
    }

    override fun observeGameState(): StateFlow<GameState?> =
        gameState

    override fun updateGameState(gameState: GameState) {
        this.gameState.value = gameState
    }
}

fun getGame(): A1Game =
    A1GameImpl()
