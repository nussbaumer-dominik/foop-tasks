package at.ac.tuwien.foop.common.models.mapper

import at.ac.tuwien.foop.common.models.domain.socket.Size
import at.ac.tuwien.foop.common.models.dtos.socket.SizeDto

fun Size.mapToDto(): SizeDto = SizeDto(
    width = width,
    height = height,
)

fun SizeDto.map(): Size = Size(
    width = width,
    height = height,
)
