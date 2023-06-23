package components

import at.ac.tuwien.foop.common.AoopMessage
import at.ac.tuwien.foop.common.GlobalMessage
import at.ac.tuwien.foop.common.PrivateMessage
import at.ac.tuwien.foop.common.domain.GameBoardDto
import at.ac.tuwien.foop.common.serializerConfig
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

class GameClient(
    private val host: String = "127.0.0.1",
    private val port: Int = 8080,
    private val onStateUpdate: (GlobalMessage.StateUpdate) -> Unit,
    private val onMapUpdate: (GameBoardDto) -> Unit,
    private val onSetupInfo: (PrivateMessage.SetupInfo) -> Unit
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

    private val commandQueue: BlockingQueue<PrivateMessage.MoveCommand> = ArrayBlockingQueue(5)

    suspend fun start() {
        client.webSocket(
            method = HttpMethod.Get,
            host = host,
            port = port,
            path = "/ws"
        ) {
            val connection = this
            val updateRoutine = launch { receive(connection) }
            val inputRoutine = launch { sendLoop(connection) }

            updateRoutine.join()
            inputRoutine.cancelAndJoin()
        }
    }

    private suspend fun sendLoop(connection: DefaultClientWebSocketSession) {
        println("inside sendLoop")
        while (true) {
            val command = commandQueue.take()
            println("Sending: $command")

            try {
                connection.sendSerialized(command as AoopMessage)
            } catch (e: ClosedSendChannelException) {
                println("Channel closed: ${connection.closeReason.await()}")
            } catch (e: Throwable) {
                println("Exception in GameClient: $e, ${connection.closeReason.await()}}")
            }

            commandQueue.clear()
        }
    }

    fun sendCommand(command: PrivateMessage.MoveCommand) {
        commandQueue.add(command)
    }

    private suspend fun receive(connection: DefaultClientWebSocketSession) {
        println("inside receive")
        try {
            while (true) {
                val incomingMessage = connection.receiveDeserialized<AoopMessage>()
                //println("Received: $incomingMessage")
                when (incomingMessage) {
                    is GlobalMessage.MapUpdate -> {
                        onMapUpdate(incomingMessage.map)
                    }

                    is GlobalMessage.StateUpdate -> {
                        onStateUpdate(incomingMessage)
                    }

                    is PrivateMessage.SetupInfo -> {
                        onSetupInfo(incomingMessage)
                    }

                    else -> println("Something else")
                }
            }
        } catch (e: ClosedSendChannelException) {
            println("Channel closed: ${connection.closeReason.await()}")
        } catch (e: Throwable) {
            println("Exception in GameClient receive: $e, ${connection.closeReason.await()}}")
        }
    }

    fun dispose() {
        client.close()
    }
}