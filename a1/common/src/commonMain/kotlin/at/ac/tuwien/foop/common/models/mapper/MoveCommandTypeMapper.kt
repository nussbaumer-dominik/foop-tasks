package at.ac.tuwien.foop.common.models.mapper

import at.ac.tuwien.foop.common.models.domain.socket.MoveCommandType
import at.ac.tuwien.foop.common.models.dtos.socket.MoveCommandTypeDto

fun MoveCommandType.mapToDto(): MoveCommandTypeDto = when (this) {
    MoveCommandType.MOVE -> MoveCommandTypeDto.MOVE
    MoveCommandType.STOP -> MoveCommandTypeDto.STOP
}

fun MoveCommandTypeDto.map(): MoveCommandType = when (this) {
    MoveCommandTypeDto.MOVE -> MoveCommandType.MOVE
    MoveCommandTypeDto.STOP -> MoveCommandType.STOP
}
