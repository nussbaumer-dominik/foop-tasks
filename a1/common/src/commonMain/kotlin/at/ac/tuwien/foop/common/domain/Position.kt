package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.abs

/**
 * Object representing the position of something on the map relative to the top left corner (0,0)
 * */
@Serializable
data class Position(
    @SerialName("x")
    var x: Int,
    @SerialName("y")
    var y: Int,
) {
    fun getNewPosition(direction: Direction): Position {
        return when (direction) {
            Direction.UP -> Position(x, y - 1)
            Direction.DOWN -> Position(x, y + 1)
            Direction.LEFT -> Position(x - 1, y)
            Direction.RIGHT -> Position(x + 1, y)
        }
    }

    fun distanceTo(position: Position): Int {
        return abs(x - position.x) + abs(y - position.y)
    }
}
