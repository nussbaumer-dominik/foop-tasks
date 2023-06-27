package at.ac.tuwien.foop.common.models.dtos.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The exit of a subway
 * */
@Serializable
data class ExitDto(
    /**
     * The top left corner position of the exit
     * */
    @SerialName("position")
    val position: PositionDto,
    /**
     * The size of the exit
     * */
    @SerialName("size")
    val size: SizeDto = SizeDto(32, 32),
    /**
     * The subway this exit belongs to
     * */
    @SerialName("subway_id")
    val subwayId: String,
)
