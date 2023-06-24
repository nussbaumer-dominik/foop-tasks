package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

/**
 * A cat controlled by a player
 * */
@Serializable
data class PlayerDto(
    /**
     * The unique id of the play assigned when they join the game
     * */
    @SerialName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerialName("sizeDto")
    val sizeDto: SizeDto = SizeDto(32, 32),
    /**
     * The current position of the player on the map relative to the top left corner
     * */
    @SerialName("positionDto")
    var positionDto: PositionDto,
    /**
     * The unique color assigned to this player to distinguish it form others
     * */
    @SerialName("color")
    val color: String,
)