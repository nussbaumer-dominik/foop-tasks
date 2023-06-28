package at.ac.tuwien.foop.util

import at.ac.tuwien.foop.common.models.dtos.socket.AoopMessageDto
import at.ac.tuwien.foop.common.models.dtos.socket.PrivateMessageDto
import at.ac.tuwien.foop.domain.Player
import at.ac.tuwien.foop.game.GameImpl
import io.ktor.server.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException

class CommandListener(
    private val connection: DefaultWebSocketServerSession,
    private val game: GameImpl,
    private val player: Player,
) {
    //TODO: maybe implement the Commands using the Command pattern: https://refactoring.guru/design-patterns/command
    suspend fun start() {
        try {
            while (true) {
                when (val incomingMessage = connection.receiveDeserialized<AoopMessageDto>()) {
                    is PrivateMessageDto.MoveCommandDto -> {
                        game.updatePlayerVelocity(
                            playerId = player.id,
                            direction = incomingMessage.direction,
                            type = incomingMessage.moveType
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