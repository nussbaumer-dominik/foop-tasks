package at.ac.tuwien.foop.common.models.dtos.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A cat controlled by a player
 * */
@Serializable
data class PlayerDto(
    /**
     * The unique id of the player assigned when they join the game
     * */
    @SerialName("id")
    val id: String,
    /**
     * The unique username of the player assigned when they join the game
     * */
    @SerialName("username")
    val username: String,
    /**
     * The current position of the player on the map relative to the top left corner
     * */
    @SerialName("position")
    var position: PositionDto,
    /**
     * The score of the player
     * */
    @SerialName("score")
    val score: Int,
    /**
     * The color of the player
     * */
    @SerialName("color")
    val color: HSLColorDto,
)
