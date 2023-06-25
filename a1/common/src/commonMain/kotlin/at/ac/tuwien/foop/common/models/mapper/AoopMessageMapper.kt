package at.ac.tuwien.foop.common.models.mapper

import at.ac.tuwien.foop.common.models.domain.socket.AoopMessage
import at.ac.tuwien.foop.common.models.domain.socket.GlobalMessage
import at.ac.tuwien.foop.common.models.domain.socket.PrivateMessage
import at.ac.tuwien.foop.common.models.dtos.socket.AoopMessageDto
import at.ac.tuwien.foop.common.models.dtos.socket.GlobalMessageDto
import at.ac.tuwien.foop.common.models.dtos.socket.PrivateMessageDto

fun AoopMessage.mapToDto(): AoopMessageDto = when (this) {
    is GlobalMessage.StateUpdate -> GlobalMessageDto.StateUpdateDto(
        map = map.mapToDto(),
        state = status.mapToDto(),
    )

    is PrivateMessage.JoinRequest -> PrivateMessageDto.JoinRequestDto(
        id = id,
    )

    is PrivateMessage.MoveCommand -> PrivateMessageDto.MoveCommandDto(
        id = id,
        direction = direction.mapToDto(),
        moveType = type.mapToDto(),
    )
}

fun AoopMessageDto.map(): AoopMessage = when (this) {
    is GlobalMessageDto.StateUpdateDto -> GlobalMessage.StateUpdate(
        map = map.map(),
        status = state.map(),
    )

    is PrivateMessageDto.JoinRequestDto -> PrivateMessage.JoinRequest(
        id = id,
    )

    is PrivateMessageDto.MoveCommandDto -> PrivateMessage.MoveCommand(
        id = id,
        direction = direction.map(),
        type = moveType.map(),
    )
}
