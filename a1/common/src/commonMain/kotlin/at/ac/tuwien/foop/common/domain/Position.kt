package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
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
    @Transient
    val moveSize: Int = 4,
) {
    fun getNewPosition(direction: Direction): Position {
        return when (direction) {
            Direction.UP -> Position(x, y - moveSize)
            Direction.DOWN -> Position(x, y + moveSize)
            Direction.LEFT -> Position(x - moveSize, y)
            Direction.RIGHT -> Position(x + moveSize, y)
        }
    }

    fun distanceTo(position: Position): Int {
        return abs(x - position.x) + abs(y - position.y)
    }
}
