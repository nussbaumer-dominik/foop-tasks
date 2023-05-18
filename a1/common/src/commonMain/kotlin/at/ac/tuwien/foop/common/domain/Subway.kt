package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Subway(
    @SerialName("position")
    val position: Position,
    @SerialName("size")
    val size: Size,
    @SerialName("exits")
    val exits: Set<Exit>,
)
