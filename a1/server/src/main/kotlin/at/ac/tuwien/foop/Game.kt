package at.ac.tuwien.foop

import at.ac.tuwien.foop.common.AoopMessage
import at.ac.tuwien.foop.common.GlobalMessage
import at.ac.tuwien.foop.common.domain.GameBoard
import at.ac.tuwien.foop.common.domain.GameState
import at.ac.tuwien.foop.util.GameBoardGenerator
import io.ktor.server.websocket.*
import kotlinx.coroutines.delay

data class Game(
    val configuration: GameConfiguration,
    var board: GameBoard? = null,
    var state: GameState = GameState.WAITING,
    val connections: MutableSet<WebSocketServerSession> = mutableSetOf(),
) {
    init {
        board = GameBoardGenerator.generateGameBoard(configuration)
    }

    fun addPlayerSession(session: WebSocketServerSession) {
        connections += session
    }

    suspend fun start() {
        state = GameState.RUNNING
        while (true) {
            delay(500)

            for (mouse in board!!.mice) {
                mouse.move(board!!)
            }
            board!!.generateGrid()

            // send update every tick to all connected players
            connections.forEach {
                it.sendSerialized(
                    GlobalMessage.StateUpdate(map = board!!, state = state) as AoopMessage,
                )
            }
        }
    }
}