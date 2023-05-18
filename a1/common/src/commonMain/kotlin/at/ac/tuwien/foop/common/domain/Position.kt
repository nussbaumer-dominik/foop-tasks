package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Position(
    @SerialName("x")
    val x: Int,
    @SerialName("y")
    val y: Int,
)
