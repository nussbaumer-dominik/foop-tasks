package at.ac.tuwien.foop.routes

import at.ac.tuwien.foop.Game
import at.ac.tuwien.foop.common.AoopMessage
import at.ac.tuwien.foop.common.GlobalMessage
import at.ac.tuwien.foop.common.PrivateMessage
import at.ac.tuwien.foop.common.domain.GameState
import at.ac.tuwien.foop.common.domain.Player
import at.ac.tuwien.foop.common.domain.Position
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

fun Application.socketEndpoint(game: Game) {
    routing {
        webSocket("/ws") {
            val player = Player(
                color = "red", position = Position(game.board.columns / 2, game.board.rows / 2)
            )

            // Send initial info
            game.addPlayerSession(this, player)
            sendSerialized(GlobalMessage.MapUpdate(map = game.board) as AoopMessage)
            sendSerialized(PrivateMessage.SetupInfo(player = player) as AoopMessage)

            // start listening for incoming messages in a separate coroutine
            val listener = launch {
                println("inside key listener for player ${player.id}")
                while (true) {
                    try {
                        for (frame in incoming) {
                            val moveCommand = receiveDeserialized<PrivateMessage.MoveCommand>()
                            println("Received on server: $moveCommand")
                            game.addMove(playerId = player.id, direction = moveCommand.direction)
                        }
                    } catch (e: ClosedReceiveChannelException) {
                        println("Channel closed: ${closeReason.await()}")
                    } catch (e: Throwable) {
                        println("Exception: $e, ${closeReason.await()}")
                        exitProcess(1)
                    }
                }
            }

            listener.start()

            // start game if not running
            if (game.state == GameState.WAITING) {
                val gameRoutine = launch {
                    game.start()
                }

                gameRoutine.join()
            }
        }
    }
}
