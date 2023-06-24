package at.ac.tuwien.foop

data class Configuration(
    val gameConfiguration: GameConfiguration
)

data class GameConfiguration(
    val width: Int = 800,
    val height: Int = 800,
    val numberOfMice: Int = 1,
    val numberOfSubways: Int = 2,
    val maxNumberOfExits: Int = 4,
    val fieldSize: Int = 32,
)
