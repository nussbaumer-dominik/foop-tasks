package at.ac.tuwien.foop.common.client.impl

import at.ac.tuwien.foop.common.client.A1SocketClient
import at.ac.tuwien.foop.common.models.domain.socket.AoopMessage
import at.ac.tuwien.foop.common.models.domain.socket.PrivateMessage
import at.ac.tuwien.foop.common.models.dtos.socket.AoopMessageDto
import at.ac.tuwien.foop.common.models.mapper.map
import at.ac.tuwien.foop.common.models.mapper.mapToDto
import at.ac.tuwien.foop.common.serializerConfig
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.Closeable

internal class KtorA1SocketClient(
    private val baseUrl: String,
    private val listener: SocketMessageListener,
) : A1SocketClient, Closeable {
    private val socketState: MutableStateFlow<SocketState> = MutableStateFlow(SocketState.CLOSED)

    private val client = HttpClient {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(
                Json {
                    serializersModule = serializerConfig
                }
            )
        }
    }

    private var isRunning: Boolean = true
    private var connection: DefaultClientWebSocketSession? = null

    override suspend fun connect() {
        coroutineScope {
            launch(Dispatchers.IO) {
                client.webSocket(
                    urlString = baseUrl,
                ) {
                    connection = this
                    socketState.value = SocketState.OPEN

                    try {
                        while (isRunning) {
                            val message = receiveDeserialized<AoopMessageDto>()
                            listener.onReceive(message.map())
                        }
                    } catch (e: ClosedSendChannelException) {
                        println("Channel closed: ${connection?.closeReason?.await()}")
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        println("Exception in socket received: $e, ${connection?.closeReason?.await()}}")
                    } finally {
                        socketState.value = SocketState.CLOSED
                    }
                }
            }
        }
    }

    override suspend fun join(message: PrivateMessage.JoinRequest) {
        sendMessage(message.mapToDto())
    }

    override suspend fun move(message: PrivateMessage.MoveCommand) {
        sendMessage(message.mapToDto())
    }

    private suspend fun sendMessage(message: AoopMessageDto) = withContext(Dispatchers.IO) {
        connection?.sendSerialized(message)
    }

    override suspend fun observeState(): StateFlow<SocketState> =
        socketState

    override suspend fun getState(): SocketState =
        socketState.value

    override fun close() {
        isRunning = false
        connection = null
        client.close()
    }
}

enum class SocketState {
    OPEN,
    CLOSED,
}

fun interface SocketMessageListener {
    fun onReceive(message: AoopMessage)
}

fun getA1SocketClient(baseUrl: String, listener: SocketMessageListener): A1SocketClient =
    KtorA1SocketClient(baseUrl, listener)
