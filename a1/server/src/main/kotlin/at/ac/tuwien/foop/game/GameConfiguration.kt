package at.ac.tuwien.foop.game

data class GameConfiguration(
    val width: Int = 800,
    val height: Int = 800,
    val numberOfMice: Int = 1,
    val numberOfSubways: Int = 3,
    val maxNumberOfExits: Int = 2,
    val fieldSize: Int = 32,
)
