package at.ac.tuwien.foop.common.models.mapper

import at.ac.tuwien.foop.common.models.domain.socket.Position
import at.ac.tuwien.foop.common.models.dtos.socket.PositionDto

fun Position.mapToDto(): PositionDto = PositionDto(
    x = x,
    y = y,
)

fun PositionDto.map(): Position = Position(
    x = x,
    y = y,
)
