package at.ac.tuwien.foop.common.models.mapper

import at.ac.tuwien.foop.common.models.domain.socket.Player
import at.ac.tuwien.foop.common.models.dtos.socket.PlayerDto

fun Player.mapToDto(): PlayerDto = PlayerDto(
    id = id,
    username = username,
    position = position.mapToDto(),
    color = color,
)

fun PlayerDto.map(): Player = Player(
    id = id,
    username = username,
    position = position.map(),
    color = color,
)
