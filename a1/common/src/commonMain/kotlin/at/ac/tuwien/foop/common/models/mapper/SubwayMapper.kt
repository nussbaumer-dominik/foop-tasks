package at.ac.tuwien.foop.common.models.mapper

import at.ac.tuwien.foop.common.models.domain.socket.Exit
import at.ac.tuwien.foop.common.models.domain.socket.Subway
import at.ac.tuwien.foop.common.models.dtos.socket.ExitDto
import at.ac.tuwien.foop.common.models.dtos.socket.SubwayDto

fun Subway.mapToDto(): SubwayDto = SubwayDto(
    id = id,
    exits = exits.map(Exit::mapToDto).toSet(),
)

fun SubwayDto.map(): Subway = Subway(
    id = id,
    exits = exits.map(ExitDto::map).toSet(),
)
