package at.ac.tuwien.foop.common.models.mapper

import at.ac.tuwien.foop.common.models.domain.socket.Direction
import at.ac.tuwien.foop.common.models.dtos.socket.DirectionDto

fun Direction.mapToDto(): DirectionDto = when (this) {
    Direction.UP -> DirectionDto.UP
    Direction.DOWN -> DirectionDto.DOWN
    Direction.LEFT -> DirectionDto.LEFT
    Direction.RIGHT -> DirectionDto.RIGHT
}

fun DirectionDto.map(): Direction = when (this) {
    DirectionDto.UP -> Direction.UP
    DirectionDto.DOWN -> Direction.DOWN
    DirectionDto.LEFT -> Direction.LEFT
    DirectionDto.RIGHT -> Direction.RIGHT
}
 