package components

import at.ac.tuwien.foop.common.domain.GameBoard
import at.ac.tuwien.foop.common.domain.MoveCommand
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json

class GameClient(
    private val host: String,
    private val port: Int,
    private val onGameStateUpdate: (GameBoard) -> Unit,
) {
    private val client = HttpClient {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }

    suspend fun send(moveCommand: MoveCommand) {
        client.webSocket(
            method = HttpMethod.Get,
            host = host,
            port = port,
            path = "/ws"
        ) {
            send(Json.encodeToString(MoveCommand.serializer(), moveCommand))
        }
    }

    suspend fun receive() {
        client.webSocket(
            method = HttpMethod.Get,
            host = host,
            port = port,
            path = "/ws"
        ) {
            while (true) {
                val message = incoming.receive() as? Frame.Text ?: continue
                val gameState = Json.decodeFromString(GameBoard.serializer(), message.readText())
                onGameStateUpdate(gameState)
            }
        }
    }

    fun dispose() {
        client.close()
    }
}