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

    @SerialName("subway")
    var subway: Subway?,

    var moveAlgorithm: (Mouse, GameBoard) -> Position,

    var catsPositions: List<Position> = mutableListOf(),
    var targetPosition: Position? = null
) : Field {
    fun move(gameBoard: GameBoard) {
        val newPosition = moveAlgorithm(this, gameBoard)
        val field = gameBoard.getFieldAtPosition(newPosition)
        if (field is Exit) {
            subway = gameBoard.subways.first {it.id == field.subwayId}
        }
        position.x = newPosition.x
        position.y = newPosition.y

    }

    override fun toChar(): Char {
        if (subway != null) {
            return 'm'
        }
        return 'M'
    }
}
