package at.ac.tuwien.foop

import at.ac.tuwien.foop.common.AoopMessage
import at.ac.tuwien.foop.common.GlobalMessage
import at.ac.tuwien.foop.common.PrivateMessage
import at.ac.tuwien.foop.common.domain.Direction
import at.ac.tuwien.foop.common.domain.GameState
import at.ac.tuwien.foop.domain.GameBoard
import at.ac.tuwien.foop.domain.Player
import at.ac.tuwien.foop.util.GameBoardGenerator
import io.ktor.server.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.delay

data class Game(
    val fps: Int = 60,
    val configuration: GameConfiguration,
    var board: GameBoard = GameBoardGenerator.generateGameBoard(configuration),
    var state: GameState = GameState.WAITING,
    val connections: MutableSet<DefaultWebSocketServerSession> = mutableSetOf(),
) {
    suspend fun addPlayerSession(session: DefaultWebSocketServerSession, player: Player) {
        connections += session
        board.players += player
        session.sendSerialized(GlobalMessage.MapUpdate(map = board.toDto()) as AoopMessage)
        session.sendSerialized(PrivateMessage.SetupInfo(playerDto = player.toDto()) as AoopMessage)
    }

    private suspend fun broadcast(message: AoopMessage) {
        connections.forEach {
            try {
                it.sendSerialized(message)
            } catch (e: ClosedReceiveChannelException) {
                println("Channel closed: $e")
                connections.remove(it)
            } catch (e: Throwable) {
                println("Exception in broadcast: $e")
                connections.remove(it)
            }
        }
    }

    fun changePlayerVelocity(playerId: String, direction: Direction, type: PrivateMessage.MoveCommandType) {
        val player = board.players.find { it.id == playerId }!!
        when (direction) {
            Direction.UP -> {
                if (type == PrivateMessage.MoveCommandType.MOVE) {
                    player.velocity = player.velocity.copy(yu = -player.position.moveSize)
                } else {
                    player.velocity = player.velocity.copy(yu = 0)
                }
            }

            Direction.DOWN -> {
                if (type == PrivateMessage.MoveCommandType.MOVE) {
                    player.velocity = player.velocity.copy(yd = player.position.moveSize)
                } else {
                    player.velocity = player.velocity.copy(yd = 0)
                }
            }

            Direction.LEFT -> {
                if (type == PrivateMessage.MoveCommandType.MOVE) {
                    player.velocity = player.velocity.copy(xl = -player.position.moveSize)
                } else {
                    player.velocity = player.velocity.copy(xl = 0)
                }
            }

            Direction.RIGHT -> {
                if (type == PrivateMessage.MoveCommandType.MOVE) {
                    player.velocity = player.velocity.copy(xr = player.position.moveSize)
                } else {
                    player.velocity = player.velocity.copy(xr = 0)
                }
            }
        }
    }

    suspend fun start() {
        println("starting game")
        val tickRate = 1000 / fps
        state = GameState.RUNNING
        while (true) {
            val currentTimeMs = System.currentTimeMillis()

            for (player in board.players) {
                player.move(width = board.width, height = board.height)
            }

            //TODO: add mouse collision
            //TODO: correctly move mouse into the subway
            //board.moveMice()
            //board.generateGrid()

            state = if (board.isWinningState()) GameState.MICE_WON else GameState.RUNNING
            val timeElapsedMs = System.currentTimeMillis() - currentTimeMs

            delay(tickRate - timeElapsedMs)

            // send update every tick to all connected players
            broadcast(
                GlobalMessage.StateUpdate(
                    map = board.toDto(),
                    state = state
                ) as AoopMessage
            )

            if (state != GameState.RUNNING) break
        }
    }
}