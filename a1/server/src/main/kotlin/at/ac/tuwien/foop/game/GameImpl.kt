package at.ac.tuwien.foop.game

import at.ac.tuwien.foop.common.models.domain.rest.RegisterRequest
import at.ac.tuwien.foop.common.models.domain.rest.RegisterResponse
import at.ac.tuwien.foop.common.models.domain.socket.*
import at.ac.tuwien.foop.common.models.dtos.socket.DirectionDto
import at.ac.tuwien.foop.common.models.dtos.socket.MoveCommandTypeDto
import at.ac.tuwien.foop.common.models.exceptions.DuplicatedUsernameException
import at.ac.tuwien.foop.common.models.exceptions.PlayerNotRegisteredException
import at.ac.tuwien.foop.common.models.mapper.mapToDto
import at.ac.tuwien.foop.domain.GameBoard
import at.ac.tuwien.foop.util.GameBoardGenerator
import io.ktor.server.websocket.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.*

data class GameImpl(
    val fps: Int = 60,
    val configuration: GameConfiguration,
) : Game {
    private val players: MutableMap<String, Player> = Collections.synchronizedMap(mutableMapOf())
    private val connections: MutableMap<String, DefaultWebSocketServerSession> =
        Collections.synchronizedMap(mutableMapOf())

    private var state: GameStatus = GameStatus.WAITING
    private var board: GameBoard = GameBoardGenerator.generateGameBoard(configuration)

    override suspend fun registerUser(registerRequest: RegisterRequest): Result<RegisterResponse> {
        println("Registering player $registerRequest")

        if (players.values.any { player -> player.username == registerRequest.username })
            return Result.failure(DuplicatedUsernameException())

        val newPlayer = Player(
            id = UUID.randomUUID().toString(),
            username = registerRequest.username,
            position = Position(0, 0),
            color = getPlayerColor(),
        )
        players[newPlayer.id] = newPlayer

        return Result.success(RegisterResponse(id = newPlayer.id, username = newPlayer.username))
    }

    private suspend fun sendGameStateUpdate() {
        sendSocketMessage(
            GlobalMessage.StateUpdate(
                map = GameBoard(
                    subways = board.subways.map { subway -> subway.map() }.toSet(),
                    mice = board.mice.map { mouse -> mouse.map() }.toSet(),
                    players = board.players.map { player -> player.map() }.toSet(),
                    width = board.width,
                    height = board.height,
                ),
                status = state,
            )
        )
    }

    private fun at.ac.tuwien.foop.domain.Player.map(): Player = Player(
        id = id,
        username = players[id]?.username ?: "Unknown",
        position = position.map(),
        color = color,
    )

    private fun at.ac.tuwien.foop.domain.Mouse.map(): Mouse = Mouse(
        id = id,
        position = position.map(),
        subway = subway?.map(),
        size = size.map(),
    )

    private fun at.ac.tuwien.foop.domain.Subway.map(): Subway = Subway(
        id = id,
        exits = exits.map { exit -> exit.map() }.toSet(),
    )

    private fun at.ac.tuwien.foop.domain.Exit.map(): Exit = Exit(
        position = position.map(),
        size = size.map(),
        subwayId = subwayId,
    )

    private fun at.ac.tuwien.foop.domain.Position.map(): Position = Position(
        x = x.toLong(),
        y = y.toLong(),
    )

    private fun at.ac.tuwien.foop.domain.Size.map(): Size = Size(
        width = width,
        height = height,
    )

    private suspend fun sendSocketMessage(message: AoopMessage) {
        println("Sending global socket message: $message")

        val connectionsToRemove = mutableSetOf<String>()

        connections.forEach { (id, connection) ->
            try {
                if (connection.isActive) {
                    connection.sendSerialized(message.mapToDto())
                } else {
                    println("Found inactive connection for $id")
                    connectionsToRemove.add(id)
                }
            } catch (e: Throwable) {
                println("Encountered problem sending to $id, ${e.message}")
                connections.remove(id)
            }
        }

        connectionsToRemove.forEach { connectionToRemove ->
            deletePlayerConnection(connectionToRemove)
        }

        if (connectionsToRemove.isNotEmpty()) {
            sendGameStateUpdate()
        }
    }

    override suspend fun removeConnection(connection: DefaultWebSocketServerSession) {
        val connectionsToRemove = connections.filter { entry -> entry.value == connection }
        connectionsToRemove.forEach { connectionToRemove ->
            deletePlayerConnection(connectionToRemove.key)
        }

        sendGameStateUpdate()
    }

    private fun deletePlayerConnection(id: String) {
        connections.remove(id)
        players.remove(id)
        board.players.removeIf { it.id == id }
    }

    override suspend fun addPlayerSession(id: String, session: DefaultWebSocketServerSession): Result<Unit> {
        if (!players.containsKey(id)) {
            return Result.failure(PlayerNotRegisteredException())
        }

        connections[id] = session
        val player = players[id]
        if (player != null) {
            board.players.add(
                at.ac.tuwien.foop.domain.Player(
                    id = player.id,
                    position = at.ac.tuwien.foop.domain.Position(player.position.x.toInt(), player.position.y.toInt()),
                    color = player.color,
                )
            )
        }

        sendGameStateUpdate()

        return Result.success(Unit)
    }

    override suspend fun startGame() {
        state = GameStatus.RUNNING
        sendGameStateUpdate()
    }

    // TODO: Fix player movement, after socket changes it is way to fast!
    override fun updatePlayerVelocity(playerId: String, direction: DirectionDto, type: MoveCommandTypeDto) {
        val player = board.players.find { it.id == playerId }!!
        when (direction) {
            DirectionDto.UP -> {
                if (type == MoveCommandTypeDto.MOVE) {
                    player.velocity = player.velocity.copy(yu = -player.moveSize)
                } else {
                    player.velocity = player.velocity.copy(yu = 0)
                }
            }

            DirectionDto.DOWN -> {
                if (type == MoveCommandTypeDto.MOVE) {
                    player.velocity = player.velocity.copy(yd = player.moveSize)
                } else {
                    player.velocity = player.velocity.copy(yd = 0)
                }
            }

            DirectionDto.LEFT -> {
                if (type == MoveCommandTypeDto.MOVE) {
                    player.velocity = player.velocity.copy(xl = -player.moveSize)
                } else {
                    player.velocity = player.velocity.copy(xl = 0)
                }
            }

            DirectionDto.RIGHT -> {
                if (type == MoveCommandTypeDto.MOVE) {
                    player.velocity = player.velocity.copy(xr = player.moveSize)
                } else {
                    player.velocity = player.velocity.copy(xr = 0)
                }
            }
        }
    }

    override suspend fun startLoop() {
        println("Starting game")

        val tickRate = 1000 / fps

        while (true) {
            val currentTimeMs = System.currentTimeMillis()

            if (state == GameStatus.RUNNING) {
                board.players.forEach { player ->
                    player.move(width = board.width, height = board.height)
                }

                board.movePlayers()
                board.checkCollisions()
                board.moveMice()

                // Check if the game is over
                checkGameState()

                sendGameStateUpdate()
            }

            val timeElapsedMs = System.currentTimeMillis() - currentTimeMs
            delay(tickRate - timeElapsedMs)
        }
    }

    private fun checkGameState() {
        if (board.mice.isEmpty()) {
            state = GameStatus.CATS_WON
        } else if (board.mice.all { m ->
                board.winningSubway!!.exits.any { e -> e.position == m.position }
            }) {
            state = GameStatus.MICE_WON
        }
    }

    // TODO: use the already existent ColorGenerator for this
    private fun getPlayerColor(): String {
        val random = Random()
        val nextInt = random.nextInt(0xffffff + 1)
        return String.format("#%06x", nextInt)
    }
}
