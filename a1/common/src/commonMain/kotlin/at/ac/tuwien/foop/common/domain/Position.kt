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
    var subwayId: String? = null
) {
    fun getNewPosition(direction: Direction, accessSubwayId: String? = null): Position {
        return when (direction) {
            Direction.UP -> Position(x, y - 1)
            Direction.DOWN -> Position(x, y + 1)
            Direction.LEFT -> Position(x - 1, y)
            Direction.RIGHT -> Position(x + 1, y)
            Direction.STAY -> Position(x, y)
            Direction.ACCESS_SUBWAY -> {
                return if (subwayId != null) {
                    Position(x, y, null)
                } else {
                    Position(x, y, accessSubwayId)
                }
            }
        }
    }

    fun distanceTo(position: Position): Int {
        val distance = abs(x - position.x) + abs(y - position.y)
        if (position.subwayId == this.subwayId) {
            // connected via subway ->  reduce distance
            return (distance / 10) + 1
        }
        return distance
    }

    fun equalCoordinates(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (x != other.x) return false
        return y == other.y
    }

    /*
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (x != other.x) return false
        return y == other.y
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
*/


}
