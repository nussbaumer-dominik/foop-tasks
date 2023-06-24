package at.ac.tuwien.foop.domain

import at.ac.tuwien.foop.common.domain.Direction
import at.ac.tuwien.foop.common.domain.PositionDto
import kotlin.math.abs

/**
 * Object representing the position of something on the map relative to the top left corner (0,0)
 * */
data class Position(
    val x: Int,
    val y: Int,
    val moveSize: Int = 4,
) {
    companion object {
        fun fromDto(dto: PositionDto): Position {
            return Position(x = dto.x, y = dto.y)
        }
    }

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

    fun toDto(): PositionDto {
        return PositionDto(x = x, y = y)
    }
}

