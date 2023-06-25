package at.ac.tuwien.foop.domain

import at.ac.tuwien.foop.common.models.dtos.socket.DirectionDto
import at.ac.tuwien.foop.common.models.dtos.socket.PositionDto
import kotlin.math.abs

/**
 * Object representing the position of something on the map relative to the top left corner (0,0)
 * */
class Position(
    var x: Int,
    var y: Int,
    val moveSize: Int = 4,
) {
    companion object {
        fun fromDto(dto: PositionDto): Position {
            return Position(x = dto.x.toInt(), y = dto.y.toInt())
        }
    }

    fun getNewPosition(direction: DirectionDto): Position {
        return when (direction) {
            DirectionDto.UP -> Position(x, y - moveSize)
            DirectionDto.DOWN -> Position(x, y + moveSize)
            DirectionDto.LEFT -> Position(x - moveSize, y)
            DirectionDto.RIGHT -> Position(x + moveSize, y)
        }
    }

    fun distanceTo(position: Position): Int {
        return abs(x - position.x) + abs(y - position.y)
    }

    fun toDto(): PositionDto {
        return PositionDto(x = x.toLong(), y = y.toLong())
    }

    fun copy(): Position {
        return Position(x, y)
    }

    fun copyWith(x: Int?, y: Int?): Position {
        return Position(
            x = x ?: this.x,
            y = y ?: this.y
        )
    }
}

