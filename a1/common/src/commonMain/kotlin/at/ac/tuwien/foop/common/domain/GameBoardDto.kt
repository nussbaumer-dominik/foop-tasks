package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The structure of the current map of the current game
 * */
@Serializable
data class GameBoardDto(
    /**
     * The subway which are positioned on the map
     * */
    @SerialName("subwayDtos")
    val subwayDtos: MutableSet<SubwayDto> = mutableSetOf(),
    @SerialName("mice")
    val mice: MutableSet<MouseDto> = mutableSetOf(),
    @SerialName("playerDtos")
    val playerDtos: MutableSet<PlayerDto> = mutableSetOf(),
    @SerialName("width")
    val width: Int = 0,
    @SerialName("height")
    val height: Int = 0,
)