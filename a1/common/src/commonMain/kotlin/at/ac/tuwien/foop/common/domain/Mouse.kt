package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * A mouse which can move across the map
 * */
@Serializable
data class Mouse(
    /**
     * The unique id of a mouse which is assigned when it is spawned in
     * */
    @SerialName("id")
    val id: String = UUID.randomUUID().toString(),
    /**
     * The current position of this mouse on the map relative to the top left corner
     * */
    @SerialName("position")
    val position: Position,

    var moveAlgorithm: (Mouse, GameBoard) -> Move,

    var catsPositions: List<Position> = mutableListOf(),
    var targetSubwayExitPosition: Position? = null,
    var nextTargetPosition: Position? = null
) : Field {
    fun move(gameBoard: GameBoard) {
        val move = moveAlgorithm(this, gameBoard)
        val field = gameBoard.getFieldAtPosition(position)
        if (move.direction == Direction.ACCESS_SUBWAY) {
            if (position.subwayId == null) {
                // if not in subway ->  go into subway
                move.newPosition.subwayId = gameBoard.subways.first { s -> s.exits.any { e -> e.position == move.newPosition } }.id
            } else {
                // if in subway -> leave subway
                position.subwayId = null
            }
        }
        position.x = move.newPosition.x
        position.y = move.newPosition.y

    }

    override fun toChar(): Char {
        if (position.subwayId != null) {
            return 'm'
        }
        return 'M'
    }
}
