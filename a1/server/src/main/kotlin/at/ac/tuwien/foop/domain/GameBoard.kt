package at.ac.tuwien.foop.domain

import at.ac.tuwien.foop.common.domain.GameBoardDto
import at.ac.tuwien.foop.common.exceptions.IllegalPositionException

/**
 * The structure of the current map of the current game
 * */
class GameBoard(
    val subways: MutableSet<Subway> = mutableSetOf(),
    val mice: MutableSet<Mouse> = mutableSetOf(),
    val players: MutableSet<Player> = mutableSetOf(),
    val width: Int = 0,
    val height: Int = 0,
    var winningSubway: Subway? = null,
    private var grid: Array<Array<Entity>>? = null, //TODO: remove it
    private var ways: Set<Way> = mutableSetOf()
) {
    companion object {
        fun fromDto(gameBoardDto: GameBoardDto): GameBoard {
            return GameBoard(
                width = gameBoardDto.width,
                height = gameBoardDto.height,
                mice = gameBoardDto.mice.map { Mouse.fromDto(it) }.toMutableSet(),
                subways = gameBoardDto.subwayDtos.map { Subway.fromDto(it) }.toMutableSet(),
                players = gameBoardDto.playerDtos.map { Player.fromDto(it) }.toMutableSet(),
            )
        }
    }

    /**
     * Adds a subway to the game board.
     * Only successful if there are not two exits on the same position
     * Returns true on success, else false
     */
    fun addSubway(subway: Subway): Boolean {
        for (s in subways) {
            if (s.exits.stream()
                    .anyMatch { e ->
                        subway.exits.stream().anyMatch { e2 ->
                            e.position == e2.position
                        }
                    }
            ) {
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
    fun isFieldEmpty(field: Entity): Boolean {
        for (s in subways) {
            for (e in s.exits) {
                val intersects = e.intersects(field)
                if (intersects) {
                    return false
                }
            }
        }

        for (p in players) {
            if (p.intersects(field)) {
                return false
            }
        }

        return true
    }

    fun moveMice() {
        for (mouse in mice) {
            mouse.move(this)
        }
    }

    fun getFieldAtPosition(position: Position): Entity {
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
        return mice.all { m ->
            winningSubway!!.exits.any { e -> e.position == m.position }
        }
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

    fun toDto(): GameBoardDto {
        return GameBoardDto(
            width = width,
            height = height,
            mice = mice.map { it.toDto() }.toMutableSet(),
            subwayDtos = subways.map { it.toDto() }.toMutableSet(),
            playerDtos = players.map { it.toDto() }.toMutableSet(),
        )
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
        return "GameBoard(subways=$subways, mice=$mice, players=$players, width=$width, height=$height, winningSubway=$winningSubway, grid=${grid?.contentToString()})"
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
            val exits = subways.first { it.id == position.subwayId }.exits.map { e ->
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
