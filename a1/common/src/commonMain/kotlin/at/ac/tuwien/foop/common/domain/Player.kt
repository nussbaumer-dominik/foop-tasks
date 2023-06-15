package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A cat controlled by a player
 * */
@Serializable
data class Player(
    /**
     * The unique id of the play assigned when they join the game
     * */
    @SerialName("id")
    val id: Long,
    /**
     * The unique color assigned to this play to distinguish it form others
     * */
    @SerialName("color")
    val color: String,
    /**
     * The current position of the player on the map relative to the top left corner
     * */
    @SerialName("position")
    val position: Position,
): Field
