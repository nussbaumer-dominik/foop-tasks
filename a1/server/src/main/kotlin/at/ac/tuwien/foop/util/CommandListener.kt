package at.ac.tuwien.foop.util

import at.ac.tuwien.foop.Game
import at.ac.tuwien.foop.common.PrivateMessage
import at.ac.tuwien.foop.domain.Player
import io.ktor.server.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlin.system.exitProcess

class CommandListener(
    private val connection: DefaultWebSocketServerSession,
    private val game: Game,
    private val player: Player,
) {
    suspend fun start() {
        println("inside key listener for player ${player.id}")
        while (true) {
            try {
                for (frame in connection.incoming) {
                    val moveCommand = connection.receiveDeserialized<PrivateMessage.MoveCommand>()
                    println("Received on server: $moveCommand")
                    game.addMove(playerId = player.id, direction = moveCommand.direction)
                }
            } catch (e: ClosedReceiveChannelException) {
                println("Channel closed: ${connection.closeReason.await()}")
            } catch (e: Throwable) {
                println("Exception in Commandlistener: $e, ${connection.closeReason.await()}")
                exitProcess(1)
            }
        }
    }
}