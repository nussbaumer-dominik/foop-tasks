package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The structure of the current map of the current game
 * */
@Serializable
data class Map(
    /**
     * The subway which are positioned on the map
     * */
    @SerialName("subways")
    val subways: Set<Subway>,
)
