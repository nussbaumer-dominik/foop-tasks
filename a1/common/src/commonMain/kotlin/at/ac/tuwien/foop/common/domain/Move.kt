package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.Serializable

@Serializable
data class Move(
    val direction: Direction,
    val newPosition: Position
) {
}
