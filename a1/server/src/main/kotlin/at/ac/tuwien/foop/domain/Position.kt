package at.ac.tuwien.foop.domain

import at.ac.tuwien.foop.common.models.dtos.socket.DirectionDto
import at.ac.tuwien.foop.common.models.dtos.socket.PositionDto
import kotlin.math.abs

/**
 * Object representing the position of something on the map relative to the top left corner (0,0)
 * */
data class Position(
    val x: Int,
    val y: Int,
    val subwayId: String? = null
) {
    companion object {
        fun fromDto(dto: PositionDto): Position {
            return Position(x = dto.x, y = dto.y)
        }
    }

    fun getNewPosition(direction: DirectionDto, moveSize: Int): Position {
        return when (direction) {
            DirectionDto.UP -> Position(x, y - moveSize, subwayId)
            DirectionDto.DOWN -> Position(x, y + moveSize, subwayId)
            DirectionDto.LEFT -> Position(x - moveSize, y, subwayId)
            DirectionDto.RIGHT -> Position(x + moveSize, y, subwayId)
        }
    }

    fun distanceTo(position: Position): Int {
        return abs(x - position.x) + abs(y - position.y)
    }

    fun toDto(): PositionDto {
        return PositionDto(x = x, y = y)
    }
}

