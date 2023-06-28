package at.ac.tuwien.foop.common.models.mapper

import at.ac.tuwien.foop.common.models.domain.socket.Mouse
import at.ac.tuwien.foop.common.models.dtos.socket.MouseDto

fun Mouse.mapToDto(): MouseDto = MouseDto(
    id = id,
    position = position.mapToDto(),
    isDead = isDead,
    subway = subway?.mapToDto(),
    size = size.mapToDto(),
)

fun MouseDto.map(): Mouse = Mouse(
    id = id,
    position = position.map(),
    isDead = isDead,
    subway = subway?.map(),
    size = size.map(),
)
 