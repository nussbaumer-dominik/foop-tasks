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
    val players: MutableSet<Player> = mutableSetOf(),
    val width: Int = 0,
    val height: Int = 0,
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
            for (e in s.exits) {
                if (position.x in e.position.x - e.size.width..e.position.x + e.size.width &&
                    position.y in e.position.y - e.size.height ..e.position.y + e.size.height
                ) {
                    return false
                }
            }
        }

        for (p in players) {
            if (position.x in p.position.x..p.position.x + p.size.width &&
                position.y in p.position.y..p.position.y + p.size.height
            ) {
                return false
            }
        }

        return true
    }

    fun generateGrid() {
        grid = Array(width + 1) { Array(height + 1) { EmptyField() } }
        for (m in mice) {
            grid!![m.position.x][m.position.y] = m
        }
        for (s in subways) {
            for (e in s.exits) {
                grid!![e.position.x][e.position.y] = e
            }
        }
        for (p in players) {
            grid!![p.position.x][p.position.y] = p
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
        for (x in 0 until height) {
            print("|")
            for (y in 0 until width) {
                val exit = exits.firstOrNull { pair -> pair.first == Position(x, y) }
                val mouse = mice.filter { pair -> pair.first == Position(x, y) }
                val player = players.firstOrNull { p -> p.position == Position(x, y) }
                if (player != null)
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
            if (x in 0 until width && y in 0 until height) {
                if (printGrid[x][y] == ' ')
                    printGrid[x][y] = '-'
            }
        }
        val myArray = Array(height) { ' ' }
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
        if (position.x < 0 || position.x >= width || position.y < 0 || position.y >= height)
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
        if (players != other.players) return false
        if (width != other.width) return false
        if (height != other.height) return false
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
        result = 31 * result + players.hashCode()
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + (winningSubway?.hashCode() ?: 0)
        result = 31 * result + (grid?.contentDeepHashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "GameBoard(subways=$subways, mice=$mice, cats=$players, width=$width, height=$height, winningSubway=$winningSubway, grid=${grid?.contentDeepToString()})"
    }
}
