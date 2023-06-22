package at.ac.tuwien.foop.common

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val serializerConfig = SerializersModule {
    polymorphic(GlobalMessage::class) {
        subclass(GlobalMessage.MapUpdate::class)
        subclass(GlobalMessage.StateUpdate::class)
    }

    polymorphic(PrivateMessage::class) {
        subclass(PrivateMessage.SetupInfo::class)
        subclass(PrivateMessage.MoveCommand::class)
    }
}
