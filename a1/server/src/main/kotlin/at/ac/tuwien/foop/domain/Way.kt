package at.ac.tuwien.foop.domain

data class Way(
    val startingPosition: Position,
    val targetPosition: Position,
    val distance: Int,
    val subwayId: String?,
    val usingExit: Boolean = false
) {
}
