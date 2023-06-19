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
    val subways: MutableSet<Subway> = mutableSetOf(),
    @SerialName("mice")
    val mice: MutableSet<Mouse> = mutableSetOf(),
    val cats: MutableSet<Player> = mutableSetOf(),
    val rows: Int,
    val columns: Int,
    var winningSubway: Subway? = null,
    var grid: Array<Array<Field>>? = null,
    private var ways: Set<Way> = mutableSetOf()
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
            grid!![m.position.y][m.position.x] = m
        }
        for (s in subways) {
            for (e in s.exits) {
                grid!![e.position.y][e.position.x] = e
            }
        }
        for (c in cats) {
            grid!![c.position.y][c.position.x] = c
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
        for ((i, subway) in subways.withIndex()) {
            subway.exits.forEach { e -> printGrid[e.position.y][e.position.x] = i.toString().first() }
        }
        for (winningExit in winningSubway!!.exits) {
            printGrid[winningExit.position.y][winningExit.position.x] = 'W'
        }
        val catHitbox = mutableSetOf<Pair<Int, Int>>()
        printGrid.forEachIndexed { y, row ->
            row.forEachIndexed { x, field ->
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
        catHitbox.forEach { (y, x) ->
            if (x in 0 until rows && y in 0 until columns) {
                if (printGrid[y][x] == ' ')
                    printGrid[y][x] = '-'
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

    fun isWinningState(): Boolean {
        return mice.all { m -> m.position.subwayId == winningSubway!!.id }
    }

    fun getSubwayExitPairs(): List<Pair<Subway, Exit>> {
        return subways.flatMap { s -> s.exits.map { e -> Pair(s, e) } }
    }

    /**
     * Gets the positions that can be reached from the given position plus their distance as int
     * The resulting list is not ordered in any way
     */
    fun getPossibleWays(position: Position): Set<Way> {
        val field = getFieldAtPosition(position)
        val possibleWays = mutableSetOf<Way>()
        if (field is Exit) {
            // standing on exit -> possibility to enter/exit the subway
            if (position.subwayId != null) {
                possibleWays.add(Way(position, position, 0, null, true))
                val subwayOfTheExit =
                    subways.first {it.id == position.subwayId}
                val waysToExits = subwayOfTheExit.exits.map { e ->
                    Way(
                        startingPosition = position,
                        targetPosition = e.position,
                        distance = e.position.distanceTo(position),
                        subwayId = position.subwayId
                    )
                }
                possibleWays.addAll(waysToExits)
            } else {
                possibleWays.add(Way(position, position, 0, field.subwayId, true))
            }
        }
        if (position.subwayId != null) {
            // currently in a subway -> only a way to the other exits in this subway
            val exits = subways.first { it.id == position.subwayId!! }.exits.map { e ->
                Way(
                    startingPosition = position,
                    targetPosition = e.position,
                    distance = e.position.distanceTo(position),
                    subwayId = position.subwayId
                )
            }
            possibleWays.addAll(exits)
        } else {
            // currently on land -> there is a way to all the exits on land
            val onLandExits = subways.flatMap { s ->
                s.exits.map { e ->
                    Way(
                        startingPosition = position,
                        targetPosition = e.position,
                        distance = e.position.distanceTo(position),
                        subwayId = s.id
                    )
                }
            }
            possibleWays.addAll(onLandExits)
        }
        println("Possible ways from $position -> $possibleWays")
        return possibleWays
    }

    /**
     * Returns true if the two positions are directly connected via a subway
     */
    fun arePositionsConnectedViaSubway(pos1: Position, pos2: Position): Boolean {
        val subwayExits = getSubwayExitPairs()
        val exit1 = subwayExits.firstOrNull() { (_, e) -> e.position == pos1 }
        val exit2 = subwayExits.firstOrNull() { (_, e) -> e.position == pos2 }
        return exit1 != null && exit2 != null && exit1.first == exit2.first
    }

    /**
     * Get all the ways (edges) from each exit position to all other exit positions.
     * Ways are bidirectional, so the way x -> y is only contained once (y -> x is not in the result set)
     * If the parameter 'startingPosition' is set then additionally all the ways are added that start from this position
     */
    fun getAllWays(startingPosition: Position? = null): Set<Way> {
        val allWays = getAllWays().toMutableSet()
        if (startingPosition != null) {
            allWays.addAll(getPossibleWays(startingPosition))
        }
        return allWays
    }

    /**
     * Get all the ways (edges) from each exit position to all other exit positions.
     * Ways are bidirectional, so the way x -> y is only contained once (y -> x is not in the result set)
     * The ways are only calculated once and then cached in the local variable 'ways'. Therefore, it must be immutable!
     */
    private fun getAllWays(): Set<Way> {
        if (ways.isNotEmpty()) return ways
        val ways = mutableSetOf<Way>()
        //add all the ways via subways
        for (subway in subways) {
            for (exit in subway.exits) {
                for (connectedExit in subway.exits) {
                    if (exit == connectedExit) continue
                    ways.add(
                        Way(
                            exit.position,
                            connectedExit.position,
                            exit.position.distanceTo(connectedExit.position),
                            subway.id
                        )
                    )
                }
            }
        }
        //add all the ways from surface to subway (enter/exit a subway)
        for (subway in subways) {
            for (exit in subway.exits) {
                ways.add(Way(exit.position, exit.position, 0, subway.id, true))  //enter a subway
                ways.add(Way(exit.position, exit.position, 0, null, true))  //exit a subway
            }
        }
        //add all the ways that are possible on the surface
        val subwayExits = getSubwayExitPairs()
        for ((_, exit) in subwayExits) {
            val waysStartingAtThatExit = mutableSetOf<Way>()
            subwayExits.forEach { (_, e) ->
                if (e.position != exit.position) {
                    waysStartingAtThatExit.add(
                        Way(
                            exit.position.copy(subwayId = null),
                            e.position,
                            exit.position.distanceTo(e.position),
                            null
                        )
                    )
                }
            }
            ways.addAll(waysStartingAtThatExit)
        }

        return ways
    }
}
