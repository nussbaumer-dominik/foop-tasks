package at.ac.tuwien.foop.common.models.mapper

import at.ac.tuwien.foop.common.models.domain.socket.Exit
import at.ac.tuwien.foop.common.models.dtos.socket.ExitDto

fun Exit.mapToDto(): ExitDto = ExitDto(
    position = position.mapToDto(),
    size = size.mapToDto(),
    subwayId = subwayId,
)

fun ExitDto.map(): Exit = Exit(
    position = position.map(),
    size = size.map(),
    subwayId = subwayId,
)
