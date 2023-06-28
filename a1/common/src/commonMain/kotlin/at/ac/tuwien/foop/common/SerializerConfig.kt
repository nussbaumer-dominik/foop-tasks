package at.ac.tuwien.foop.common

import at.ac.tuwien.foop.common.models.dtos.socket.GlobalMessageDto
import at.ac.tuwien.foop.common.models.dtos.socket.PrivateMessageDto
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val serializerConfig = SerializersModule {
    polymorphic(GlobalMessageDto::class) {
        subclass(GlobalMessageDto.StateUpdateDto::class)
    }

    polymorphic(PrivateMessageDto::class) {
        subclass(PrivateMessageDto.JoinRequestDto::class)
        subclass(PrivateMessageDto.MoveCommandDto::class)
    }
}
