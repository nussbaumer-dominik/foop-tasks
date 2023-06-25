package at.ac.tuwien.foop.domain

data class Way(
    val startingEntity: Entity,
    val targetEntity: Entity,
    val distance: Int,
    // TODO: remove this property if it is not needed!
    val subwayId: String?,
    // TODO: remove this property if it is not needed!
    val usingExit: Boolean = false
) {
}
