package at.ac.tuwien.foop

data class Configuration(
    val gameConfiguration: GameConfiguration
)

data class GameConfiguration(
    val width: Int = 400,
    val height: Int = 400,
    val numberOfMice: Int = 10,
    val numberOfSubways: Int = 10,
    val maxNumberOfExits: Int = 4,
    val fieldSize: Int = 32,
)