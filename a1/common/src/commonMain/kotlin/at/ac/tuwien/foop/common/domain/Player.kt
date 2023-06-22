package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

/**
 * A cat controlled by a player
 * */
@Serializable
data class Player(
    /**
     * The unique id of the play assigned when they join the game
     * */
    @SerialName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerialName("size")
    val size: Size = Size(32, 32),
    /**
     * The unique color assigned to this player to distinguish it form others
     * */
    @SerialName("color")
    val color: String,
    /**
     * The current position of the player on the map relative to the top left corner
     * */
    @SerialName("position")
    var position: Position,
) : Field {

    fun move(direction: Direction) {
        position = position.getNewPosition(direction)
    }

    override fun toChar(): Char {
        return '#'
    }
}
