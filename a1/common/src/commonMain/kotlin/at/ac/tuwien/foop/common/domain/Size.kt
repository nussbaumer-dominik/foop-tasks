package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The size of an object positioned on the map
 * */
@Serializable
data class Size(
    @SerialName("width")
    val width: Int,
    @SerialName("height")
    val height: Int,
)
