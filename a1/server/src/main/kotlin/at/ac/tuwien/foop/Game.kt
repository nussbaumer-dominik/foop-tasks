package at.ac.tuwien.foop

import at.ac.tuwien.foop.common.AoopMessage
import at.ac.tuwien.foop.common.GlobalMessage
import at.ac.tuwien.foop.common.PrivateMessage
import at.ac.tuwien.foop.common.domain.Direction
import at.ac.tuwien.foop.common.domain.GameBoard
import at.ac.tuwien.foop.common.domain.GameState
import at.ac.tuwien.foop.common.domain.Player
import at.ac.tuwien.foop.util.GameBoardGenerator
import io.ktor.server.websocket.*
import kotlinx.coroutines.delay

data class Game(
    val fps: Int = 30,
    val configuration: GameConfiguration,
    var board: GameBoard = GameBoardGenerator.generateGameBoard(configuration),
    var state: GameState = GameState.WAITING,
    val connections: MutableSet<WebSocketServerSession> = mutableSetOf(),
    val currentMoves: MutableMap<String, MutableList<Direction>> = mutableMapOf(),
) {
    suspend fun addPlayerSession(session: WebSocketServerSession, player: Player) {
        connections += session
        board.players += player
        session.sendSerialized(GlobalMessage.MapUpdate(map = board) as AoopMessage)
        session.sendSerialized(PrivateMessage.SetupInfo(player = player) as AoopMessage)
    }

    fun addMove(playerId: String, direction: Direction) {
        println("adding move $direction for player $playerId")
        currentMoves.getOrPut(playerId) { mutableListOf() } += direction
    }

    suspend fun start() {
        val tickRate = 1000 / fps
        state = GameState.RUNNING
        while (true) {
            val currentTimeMs = System.currentTimeMillis()

            for (currentMove in currentMoves) {
                val player = board.players.find { it.id == currentMove.key }!!
                for (move in currentMove.value) {
                    val direction = currentMove.value.lastOrNull() ?: continue
                    player.move(direction)
                }
            }

            // TODO: add mouse collision
            currentMoves.clear()
            //TODO: correctly move mouse into the subway
            board.moveMice()
            //board.generateGrid()

            state = if (board.isWinningState()) GameState.MICE_WON else GameState.RUNNING
            val timeElapsedMs = System.currentTimeMillis() - currentTimeMs
            println("time elapsed: $timeElapsedMs")
            delay(tickRate - timeElapsedMs)

            // send update every tick to all connected players
            connections.forEach {
                it.sendSerialized(
                    GlobalMessage.StateUpdate(map = board, state = state) as AoopMessage,
                )
            }

            if (state != GameState.RUNNING) break
        }
    }
}