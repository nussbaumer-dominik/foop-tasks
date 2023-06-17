package at.ac.tuwien.foop.common.domain

/**
 * A tile of the board
 */
data class Tile(
    /**
     * The starting position of the tile
     * */
    val position: Position,
    /**
     * The width of the tile
     * */
    val width: Int,
)