package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Size(
    @SerialName("width")
    val width: Int,
    @SerialName("height")
    val height: Int
)
