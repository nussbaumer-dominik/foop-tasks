package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    @SerialName("id")
    val id: Long,
    @SerialName("color")
    val color: String,
    @SerialName("position")
    val position: Position,
)
