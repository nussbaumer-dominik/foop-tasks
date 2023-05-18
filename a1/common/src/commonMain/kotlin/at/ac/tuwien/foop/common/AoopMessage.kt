package at.ac.tuwien.foop.common

import at.ac.tuwien.foop.common.domain.Map
import at.ac.tuwien.foop.common.domain.Mouse
import at.ac.tuwien.foop.common.domain.Player
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface AoopMessage

@Serializable
@SerialName("global")
sealed interface GlobalMessage : AoopMessage {
    @Serializable
    @SerialName("global_map_update")
    data class MapUpdate(
        @SerialName("map")
        val map: Map,
    ) : GlobalMessage

    @Serializable
    @SerialName("global_state_update")
    data class StateUpdate(
        @SerialName("players")
        val players: Set<Player>,
        @SerialName("mice")
        val mice: Set<Mouse>,
    ) : GlobalMessage
}

@Serializable
@SerialName("private")
sealed interface PrivateMessage : AoopMessage {
    @Serializable
    @SerialName("private_setup_info")
    data class SetupInfo(
        @SerialName("player")
        val player: Player,
    ) : PrivateMessage
}
