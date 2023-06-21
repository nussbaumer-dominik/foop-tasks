package at.ac.tuwien.foop.common.domain

import at.ac.tuwien.foop.common.exceptions.IllegalPositionException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * The structure of the current map of the current game
 * */
@Serializable
data class GameBoard(
    /**
     * The subway which are positioned on the map
     * */
    @SerialName("subways")
    val subways: MutableSet<Subway> = mutableSetOf(),
    @SerialName("mice")
    val mice: MutableSet<Mouse> = mutableSetOf(),
    @SerialName("cats")
    val cats: MutableSet<Player> = mutableSetOf(),
    val rows: Int = 0,
    val columns: Int = 0,
    @Transient
    var winningSubway: Subway? = null,
    @Transient
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
        for (c in cats) {
            if (c.position == position) {
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
        for (c in cats) {
            grid!![c.position.x][c.position.y] = c
        }
    }

    fun moveMice() {
        for (mouse in mice) {
            mouse.move(this)
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
                val cat = cats.firstOrNull { c -> c.position == Position(x, y) }
                if (cat != null)
                    print("#|")
                else {
                    if (exit != null) {
                        if (mouse.isNotEmpty())
                            print(mouse.size.toString() + "|")
                        else
                            print(exit.second + "|")
                    } else if (mouse.isNotEmpty()) {
                        print("M|")
                    } else
                        print(" |")
                }
            }
            print("\n")
        }
    }

    fun printGrid() {
        generateGrid()
        val printGrid = grid!!.clone().map { row -> row.map { it.toChar() }.toMutableList() }.toMutableList()
        val catHitbox = mutableSetOf<Pair<Int, Int>>()
        printGrid.forEachIndexed { x, row ->
            row.forEachIndexed { y, field ->
                if (field == '#') {
                    catHitbox.add(Pair(x - 1, y))
                    catHitbox.add(Pair(x - 1, y + 1))
                    catHitbox.add(Pair(x - 1, y - 1))
                    catHitbox.add(Pair(x, y + 1))
                    catHitbox.add(Pair(x, y - 1))
                    catHitbox.add(Pair(x + 1, y))
                    catHitbox.add(Pair(x + 1, y + 1))
                    catHitbox.add(Pair(x + 1, y - 1))
                }
            }
        }
        catHitbox.forEach { (x, y) ->
            if (x >= 0 && x < rows && y >= 0 && y < columns) {
                if (printGrid[x][y] == ' ')
                    printGrid[x][y] = '-'
            }
        }
        val myArray = Array(rows) { ' ' }
        myArray[2] = 'a'
        //print grid
        printGrid.forEach { row ->
            print("|")
            row.forEach { field ->
                print("${field}|")
            }
            println()
        }
    }

    fun getFieldAtPosition(position: Position): Field {
        if (position.x < 0 || position.x >= rows || position.y < 0 || position.y >= columns)
            throw IllegalPositionException("Position is out of bounds")

        return grid!![position.x][position.y]
    }

    fun selectWinningSubway() {
        val random = (0 until subways.size).random()
        winningSubway = subways.elementAt(random)
    }

    // TODO: Return MICE_WON or CATS_WON instead of boolean if mice have won
    fun isWinningState(): Boolean {
        return mice.all { m -> winningSubway!!.exits.any { e -> e.position == m.position } }
    }

    fun getSubwayExitPairs(): List<Pair<Subway, Exit>> {
        return subways.flatMap { s -> s.exits.map { e -> Pair(s, e) } }
    }

    fun getPossiblePositions(position: Position): List<Pair<Position, Int>> {
        val field = getFieldAtPosition(position)
        //TODO: implement
        if (field is Exit) {

        }

        return emptyList()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameBoard

        if (subways != other.subways) return false
        if (mice != other.mice) return false
        if (cats != other.cats) return false
        if (rows != other.rows) return false
        if (columns != other.columns) return false
        if (winningSubway != other.winningSubway) return false
        if (grid != null) {
            if (other.grid == null) return false
            if (!grid.contentDeepEquals(other.grid)) return false
        } else if (other.grid != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = subways.hashCode()
        result = 31 * result + mice.hashCode()
        result = 31 * result + cats.hashCode()
        result = 31 * result + rows
        result = 31 * result + columns
        result = 31 * result + (winningSubway?.hashCode() ?: 0)
        result = 31 * result + (grid?.contentDeepHashCode() ?: 0)
        return result
    }
}
