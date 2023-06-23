package at.ac.tuwien.foop.common.domain

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
    @SerialName("positionDto")
    val positionDto: PositionDto,
    @SerialName("sizeDto")
    val sizeDto: SizeDto = SizeDto(32, 32),
    val subwayId: String
)
