package at.ac.tuwien.foop.common.models.dtos.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A mouse which can move across the map
 * */
@Serializable
data class MouseDto(
    /**
     * The unique id of a mouse which is assigned when it is spawned in
     * */
    @SerialName("id")
    val id: String,
    /**
     * The current position of this mouse on the map relative to the top left corner
     * */
    @SerialName("position")
    val position: PositionDto,
    /**
     * The subway the mouse is currently in
     * */
    @SerialName("subway")
    var subway: SubwayDto?,
    /**
     * The size of the mouse
     * */
    @SerialName("size")
    val size: SizeDto = SizeDto(32, 32),
)
