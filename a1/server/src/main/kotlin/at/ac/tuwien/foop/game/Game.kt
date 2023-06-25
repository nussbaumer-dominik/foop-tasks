package at.ac.tuwien.foop.game

import at.ac.tuwien.foop.common.models.domain.rest.RegisterRequest
import at.ac.tuwien.foop.common.models.domain.rest.RegisterResponse
import at.ac.tuwien.foop.common.models.dtos.socket.DirectionDto
import at.ac.tuwien.foop.common.models.dtos.socket.MoveCommandTypeDto
import io.ktor.server.websocket.*

interface Game {
    suspend fun registerUser(registerRequest: RegisterRequest): Result<RegisterResponse>
    suspend fun removeConnection(connection: DefaultWebSocketServerSession)
    suspend fun addPlayerSession(id: String, session: DefaultWebSocketServerSession): Result<Unit>
    fun changePlayerVelocity(playerId: String, direction: DirectionDto, type: MoveCommandTypeDto)
    suspend fun startGame()
    suspend fun startLoop()
}
