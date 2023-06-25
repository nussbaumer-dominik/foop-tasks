package at.ac.tuwien.foop.domain

import at.ac.tuwien.foop.common.domain.Direction

data class Move(
    val direction: Direction?,
    val actions: MouseActions?,
    val newPosition: Position
) {
}
