package at.ac.tuwien.foop.routes

import at.ac.tuwien.foop.common.models.dtos.socket.AoopMessageDto
import at.ac.tuwien.foop.common.models.dtos.socket.PrivateMessageDto
import at.ac.tuwien.foop.game.Game
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*

fun Application.socketEndpoint(game: Game) {
    routing {
        webSocket("/ws") {
            try {
                val response = awaitRegistration(game)
                if (response.isFailure) return@webSocket

                while (true) {
                    when (val incomingMessage = receiveDeserialized<AoopMessageDto>()) {
                        is PrivateMessageDto.MoveCommandDto -> {
                            game.updatePlayerVelocity(
                                playerId = incomingMessage.id,
                                direction = incomingMessage.direction,
                                type = incomingMessage.moveType
                            )
                        }

                        else -> println("Received unknown message: $incomingMessage")
                    }
                }
            } catch (e: Exception) {
                println("An error occurred with a connection: ${e.message}")
            } finally {
                game.removeConnection(this)
            }
        }
    }
}

private suspend fun DefaultWebSocketServerSession.awaitRegistration(game: Game): Result<Unit> {
    val message = receiveDeserialized<AoopMessageDto>()
    if (message !is PrivateMessageDto.JoinRequestDto) {
        close(
            reason = CloseReason(
                code = CloseReason.Codes.PROTOCOL_ERROR,
                message = "The first message has to be a join request",
            )
        )
        return Result.failure(Exception("Invalid first socket message"))
    }

    game.addPlayerSession(message.id, this)
    return Result.success(Unit)
}
