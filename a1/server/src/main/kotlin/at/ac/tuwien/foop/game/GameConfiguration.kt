package at.ac.tuwien.foop.game

data class GameConfiguration(
    val width: Int = 800,
    val height: Int = 800,
    val numberOfMice: Int = 2,
    val numberOfSubways: Int = 10,
    val maxNumberOfExits: Int = 4,
    val fieldSize: Int = 32,
)
