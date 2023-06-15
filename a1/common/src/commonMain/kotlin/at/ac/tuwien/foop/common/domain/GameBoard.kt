package at.ac.tuwien.foop.common.domain

import at.ac.tuwien.foop.common.exceptions.IllegalPositionException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The structure of the current map of the current game
 * */
@Serializable
data class GameBoard(
    /**
     * The subway which are positioned on the map
     * */
    @SerialName("subways")
    private val subways: MutableSet<Subway> = mutableSetOf(),
    @SerialName("mice")
    val mice: MutableSet<Mouse> = mutableSetOf(),
    val rows: Int,
    val columns: Int,
    var winningSubway: Subway? = null,
    var grid: Array<Array<Field>>? = null
) {
    /**
     * Adds a subway to the game board.
     * Only successful if there are not two exits on the same position
     * Returns true on success, else false
     */
    fun addSubway(subway: Subway): Boolean {
        for (s in subways) {
            if (s.exits.stream().anyMatch { e -> subway.exits.stream().anyMatch { e2 -> e.position == e2.position } }) {
                return false
            }
        }

        subways.add(subway)
        return true
    }

    /**
     * Compares the position of all subway exits to the given position
     * Returns true if a tile is empty, else false
     */
    fun isFieldEmpty(position: Position): Boolean {
        for (s in subways) {
            if (s.exits.stream().anyMatch { e -> e.position == position }) {
                return false
            }
        }
        return true
    }

    fun generateGrid() {
        grid = Array(rows) { Array(columns) { EmptyField() } }
        for (m in mice) {
            grid!![m.position.x][m.position.y] = m
        }
        for (s in subways) {
            for (e in s.exits) {
                grid!![e.position.x][e.position.y] = e
            }
        }
    }

    fun print() {
        val letters = ('A'..'Z').toMutableList()
        val mySubways = mutableListOf<Pair<Subway, Char>>()
        for (i in 0 until subways.size) {
            mySubways.add(Pair(subways.elementAt(i), letters.elementAt(i)))
        }
        val exits = mySubways.flatMap { (s, c) -> s.exits.map { e -> Pair(e.position, c) } }
        val mice = mice.map { m -> Pair(m.position, 'M') }
        for (x in 0 until rows) {
            print("|")
            for (y in 0 until columns) {
                val exit = exits.firstOrNull { pair -> pair.first == Position(x, y) }
                val mouse = mice.filter { pair -> pair.first == Position(x, y) }
                if (exit != null) {
                    if (mouse.isNotEmpty())
                        print(mouse.size.toString() + "|")
                    else
                        print(exit.second + "|")
                } else
                    print(" |")
            }
            print("\n")
        }
    }

    fun getFieldAtPosition(position: Position): Field {
        if (position.x < 0 || position.x >= rows || position.y < 0 || position.y >= columns)
            throw IllegalPositionException("Position is out of bounds")

        return grid!![position.x][position.y]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameBoard

        if (subways != other.subways) return false
        if (rows != other.rows) return false
        if (columns != other.columns) return false
        if (winningSubway != other.winningSubway) return false
        return grid.contentDeepEquals(other.grid)
    }

    override fun hashCode(): Int {
        var result = subways.hashCode()
        result = 31 * result + rows
        result = 31 * result + columns
        result = 31 * result + winningSubway.hashCode()
        result = 31 * result + grid.contentDeepHashCode()
        return result
    }

    fun selectWinningSubway() {
        val random = (0 until subways.size).random()
        winningSubway = subways.elementAt(random)
    }

    fun getExitPositions(): Set<Position> {
        val exits = mutableSetOf<Position>()
        for (s in subways) {
            for (e in s.exits) {
                exits.add(e.position)
            }
        }
        return exits
    }
}
