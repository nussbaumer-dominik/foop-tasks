package at.ac.tuwien.foop.common.models.dtos.socket

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
    @SerialName("subways")
    val subways: Set<SubwayDto>,
    /**
     * The mice with their positions
     * */
    @SerialName("mice")
    val mice: Set<MouseDto>,
    /**
     * The mice with their positions
     * */
    @SerialName("players")
    val players: Set<PlayerDto>,
    /**
     * The width of the board
     * */
    @SerialName("width")
    val width: Int,
    /**
     * The height of the board
     * */
    @SerialName("height")
    val height: Int,
)
