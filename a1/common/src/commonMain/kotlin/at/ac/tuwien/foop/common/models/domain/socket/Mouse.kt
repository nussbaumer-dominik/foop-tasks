package at.ac.tuwien.foop.common.models.domain.socket

data class Mouse(
    val id: String,
    val position: Position,
    val isDead: Boolean,
    var subway: Subway?,
    val size: Size,
)
