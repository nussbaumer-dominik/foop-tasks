package at.ac.tuwien.foop.common.domain

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
    val rows: Int,
    val columns: Int
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
    fun isTileEmpty(position: Position): Boolean {
        for (s in subways) {
            if (s.exits.stream().anyMatch { e -> e.position == position }) {
                return false
            }
        }
        return true
    }

    fun print() {
        val letters = ('A'..'Z').toMutableList()
        val mySubways = mutableListOf<Pair<Subway, Char>>()
        for (i in 0 until subways.size) {
            mySubways.add(Pair(subways.elementAt(i), letters.elementAt(i)))
        }
        val exits = mySubways.flatMap { (s, c) -> s.exits.map { e -> Pair(e, c) } }
        for (x in 0 until rows) {
            print("|")
            for (y in 0 until columns) {
                val res = exits.filter { pair -> pair.first.position == Position(x, y) }.firstOrNull()
                if (res != null) {
                    print(res.second + "|")
                } else
                    print(" |")
            }
            print("\n")
        }
    }
}
