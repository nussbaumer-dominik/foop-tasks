package at.ac.tuwien.foop.common.client

import at.ac.tuwien.foop.common.client.impl.SocketState
import at.ac.tuwien.foop.common.models.domain.socket.PrivateMessage
import kotlinx.coroutines.flow.StateFlow
import java.io.Closeable

interface A1SocketClient : Closeable {
    suspend fun connect()
    suspend fun join(message: PrivateMessage.JoinRequest)
    suspend fun move(message: PrivateMessage.MoveCommand)

    suspend fun observeState(): StateFlow<SocketState>
    suspend fun getState(): SocketState
}
