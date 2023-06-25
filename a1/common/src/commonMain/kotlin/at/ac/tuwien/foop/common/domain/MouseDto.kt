package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

/**
 * A mouse which can move across the map
 * */
@Serializable
data class MouseDto(
    /**
     * The unique id of a mouse which is assigned when it is spawned in
     * */
    @SerialName("id")
    val id: String = UUID.randomUUID().toString(),
    /**
     * The current position of this mouse on the map relative to the top left corner
     * */
    @SerialName("positionDto")
    val positionDto: PositionDto,
    @SerialName("sizeDto")
    val sizeDto: SizeDto = SizeDto(32, 32),
    @SerialName("subwayDto")
    var subwayDto: SubwayDto?,
)
