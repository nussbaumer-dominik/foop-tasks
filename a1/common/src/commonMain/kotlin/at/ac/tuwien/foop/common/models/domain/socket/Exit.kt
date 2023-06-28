package at.ac.tuwien.foop.common.models.domain.socket

data class Exit(
    val position: Position,
    val size: Size,
    val subwayId: String,
)
