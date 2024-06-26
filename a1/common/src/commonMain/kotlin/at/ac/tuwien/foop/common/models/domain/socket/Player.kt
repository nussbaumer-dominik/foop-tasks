package at.ac.tuwien.foop.common.models.domain.socket

data class Player(
    val id: String,
    val username: String,
    val position: Position,
    val score: Int,
    val color: HSLColor,
)
