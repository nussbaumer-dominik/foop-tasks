package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The exit of a subway
 * */
@Serializable
data class Exit(
    /**
     * The starting position of the exit
     * */
    @SerialName("position")
    val position: Position,
)
