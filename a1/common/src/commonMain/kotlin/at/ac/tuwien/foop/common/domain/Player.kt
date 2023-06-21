package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

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
    /**
     * The unique color assigned to this player to distinguish it form others
     * */
    @SerialName("color")
    val color: String,
    /**
     * The current position of the player on the map relative to the top left corner
     * */
    @SerialName("position")
    val position: Position,
) : Field {
    override fun toChar(): Char {
        return '#'
    }
}
