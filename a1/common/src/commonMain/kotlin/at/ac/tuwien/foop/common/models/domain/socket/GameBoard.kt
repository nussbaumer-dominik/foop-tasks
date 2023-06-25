package at.ac.tuwien.foop.common.models.domain.socket

data class GameBoard(
    val subways: Set<Subway>,
    val mice: Set<Mouse>,
    val players: Set<Player>,
    val width: Int,
    val height: Int,
)
