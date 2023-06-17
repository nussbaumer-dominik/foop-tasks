package components

import at.ac.tuwien.foop.common.AoopMessage
import at.ac.tuwien.foop.common.GlobalMessage
import at.ac.tuwien.foop.common.PrivateMessage
import at.ac.tuwien.foop.common.domain.GameBoard
import at.ac.tuwien.foop.common.domain.MoveCommand
import at.ac.tuwien.foop.common.serializerConfig
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
            contentConverter = KotlinxWebsocketSerializationConverter(
                Json {
                    serializersModule = serializerConfig
                }
            )
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
                when (val incomingMessage = receiveDeserialized<AoopMessage>()) {
                    is GlobalMessage.MapUpdate -> {
                        println(incomingMessage)
                        onGameStateUpdate(incomingMessage.map)
                    }

                    is GlobalMessage.StateUpdate -> TODO()
                    is PrivateMessage.SetupInfo -> TODO()
                    else -> println("Something else")
                }
            }
        }
    }

    fun dispose() {
        client.close()
    }
}