package at.ac.tuwien.foop

data class Configuration(
    val gameConfiguration: GameConfiguration
)

data class GameConfiguration(
    val rows: Int = 20,
    val columns: Int = 20,
    val numberOfMice: Int = 10,
    val numberOfSubways: Int = 10,
    val maxNumberOfExits: Int = 4,
)