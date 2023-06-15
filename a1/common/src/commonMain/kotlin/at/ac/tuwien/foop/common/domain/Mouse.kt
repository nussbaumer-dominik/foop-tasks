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

    @SerialName("inTube")
    var inTube: Boolean,

    var catsPositions: List<Position> = mutableListOf()
): Field {
    fun move(moveAlgorithm: (Mouse, GameBoard) -> Position, gameBoard: GameBoard) {
        val newPosition = moveAlgorithm(this, gameBoard)
        if (gameBoard.isFieldEmpty(newPosition)) {
            if (gameBoard.getFieldAtPosition(newPosition) is Exit) {
                inTube = true
            }
            position.x = newPosition.x
            position.y = newPosition.y
        }
    }
}
