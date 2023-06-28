package at.ac.tuwien.foop.domain

import at.ac.tuwien.foop.common.models.dtos.socket.DirectionDto

data class Move(
    val direction: DirectionDto?,
    val actions: MouseActions?,
    val newPosition: Position
)
