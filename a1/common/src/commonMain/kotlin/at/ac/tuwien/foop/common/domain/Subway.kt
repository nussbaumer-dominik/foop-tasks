package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A subway which is positioned on the map and has its exits
 * */
@Serializable
data class Subway(
    /**
     * The top left corner of this subway
     * */
    @SerialName("position")
    val position: Position,
    /**
     * The size of this subway
     * */
    @SerialName("size")
    val size: Size,
    /**
     * The exits that exist on this subway
     * */
    @SerialName("exits")
    val exits: Set<Exit>,
)
