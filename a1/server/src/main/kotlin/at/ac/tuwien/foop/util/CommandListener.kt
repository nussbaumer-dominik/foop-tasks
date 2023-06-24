package at.ac.tuwien.foop.util

import at.ac.tuwien.foop.Game
import at.ac.tuwien.foop.common.AoopMessage
import at.ac.tuwien.foop.common.PrivateMessage
import at.ac.tuwien.foop.domain.Player
import io.ktor.server.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException

class CommandListener(
    private val connection: DefaultWebSocketServerSession,
    private val game: Game,
    private val player: Player,
) {
    //TODO: maybe implement the Commands using the Command pattern: https://refactoring.guru/design-patterns/command
    suspend fun start() {
        println("inside key listener for player ${player.id}")
        try {
            while (true) {
                when (val incomingMessage = connection.receiveDeserialized<AoopMessage>()) {
                    is PrivateMessage.MoveCommand -> {
                        game.changePlayerVelocity(
                            playerId = player.id,
                            direction = incomingMessage.direction,
                            type = incomingMessage.type
                        )
                    }

                    else -> println("Received unknown message: $incomingMessage")
                }
            }
        } catch (e: ClosedReceiveChannelException) {
            println("Channel closed: ${connection.closeReason.await()}")
        } catch (e: Throwable) {
            println("Exception in Commandlistener: $e, ${connection.closeReason.await()}")
        }
    }
}