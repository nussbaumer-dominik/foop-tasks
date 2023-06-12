package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A mouse which can move across the map
 * */
@Serializable
data class Mouse(
    /**
     * The unique id of a mouse which is assigned when it is spawned in
     * */
    @SerialName("id")
    val id: Long,
    /**
     * The current position of this mouse on the map relative to the top left corner
     * */
    @SerialName("position")
    val position: Position,
)
