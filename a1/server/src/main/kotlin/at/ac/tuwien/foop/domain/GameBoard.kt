package at.ac.tuwien.foop.domain

import at.ac.tuwien.foop.common.models.dtos.socket.GameBoardDto

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
    private var ways: Set<Way> = mutableSetOf()
) {
    companion object {
        fun fromDto(gameBoardDto: GameBoardDto): GameBoard {
            return GameBoard(
                width = gameBoardDto.width,
                height = gameBoardDto.height,
                mice = gameBoardDto.mice.map { Mouse.fromDto(it) }.toMutableSet(),
                subways = gameBoardDto.subways.map { Subway.fromDto(it) }.toMutableSet(),
                players = gameBoardDto.players.map { Player.fromDto(it) }.toMutableSet(),
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
        for (mouse in mice.filter { !it.isDead }) {
            mouse.move(this)
        }
    }

    fun movePlayers() {
        for (player in players) {
            player.move(width = width, height = height)
        }
    }

    fun selectWinningSubway() {
        val random = (0 until subways.size).random()
        winningSubway = subways.elementAt(random)
    }

    fun getSubwayExitPairs(): List<Pair<Subway, Exit>> {
        return subways.flatMap { s -> s.exits.map { e -> Pair(s, e) } }
    }

    /**
     * Gets the positions that can be reached from the given position plus their distance as int
     * The resulting list is not ordered in any way
     */
    fun getPossibleWays(entity: Entity): Set<Way> {
        val possibleWays = mutableSetOf<Way>()
        val subwayExitPair = getSubwayExitPairs().find { (s, e) -> entity.intersects(e) }
        if (subwayExitPair != null) {
            // intersecting with an exit -> possibility to enter/exit the subway
            if (entity.position.subwayId != null) {
                // entity in a subway -> possibility to exit the subway
                possibleWays.add(
                    Way(
                        entity,
                        ConcreteEntity(entity).copyWith(position = entity.position.copy(subwayId = null)),
                        0,
                        null,
                        true
                    )
                )
                val subwayOfTheExit = subwayExitPair.second
                val waysToExits = subwayExitPair.first.exits.map { e ->
                    Way(
                        startingEntity = entity,
                        targetEntity = e,
                        distance = e.position.distanceTo(entity.position),
                        subwayId = e.subwayId
                    )
                }
                possibleWays.addAll(waysToExits)
            } else {
                // entity not in a subway -> possibility to enter the subway
                possibleWays.add(Way(entity, subwayExitPair.second, 0, subwayExitPair.second.subwayId, true))
            }
        }
        if (entity.position.subwayId != null) {
            // currently in a subway -> only a way to the other exits in this subway
            val exits = subways.first { it.id == entity.position.subwayId }.exits.map { e ->
                Way(
                    startingEntity = entity,
                    targetEntity = e,
                    distance = e.position.distanceTo(entity.position),
                    subwayId = entity.position.subwayId
                )
            }
            possibleWays.addAll(exits)
        } else {
            // currently on land -> there is a way to all the exits on land
            val onLandExits = subways.flatMap { s ->
                s.exits.map { e ->
                    Way(
                        startingEntity = entity,
                        targetEntity = ConcreteEntity(e).copyWith(position = e.position.copy(subwayId = null)),
                        distance = e.position.distanceTo(entity.position),
                        subwayId = s.id
                    )
                }
            }
            possibleWays.addAll(onLandExits)
        }
        println("Possible ways from $entity -> $possibleWays")
        return possibleWays
    }

    /**
     * Get all the ways (edges) from each exit position to all other exit positions.
     * Ways are bidirectional, so the way x -> y is only contained once (y -> x is not in the result set)
     * If the parameter 'startingPosition' is set then additionally all the ways are added that start from this position
     */
    fun getAllWays(entity: Entity? = null): Set<Way> {
        val allWays = getAllWays().toMutableSet()
        if (entity != null) {
            allWays.addAll(getPossibleWays(entity))
        }
        return allWays
    }

    /**
     * Get all the ways (edges) from each exit position to all other exit positions.
     * Ways are bidirectional, so the way x -> y is only contained once (y -> x is not in the result set)
     * The ways are only calculated once and then cached in the local variable 'ways'. Therefore, it must be immutable!
     */
    //TODO: refactor this
    private fun getAllWays(): Set<Way> {
        if (ways.isNotEmpty()) return ways
        val ways = mutableSetOf<Way>()
        //add all the ways via subways
        for (subway in subways) {
            for (exit in subway.exits) {
                for (connectedExit in subway.exits) {
                    if (exit == connectedExit) continue
                    /*ways.add(
                        Way(
                            exit.position,
                            connectedExit.position,
                            exit.position.distanceTo(connectedExit.position),
                            subway.id
                        )
                    )*/
                }
            }
        }
        //add all the ways from surface to subway (enter/exit a subway)
        for (subway in subways) {
            for (exit in subway.exits) {
                //ways.add(Way(exit.position, exit.position, 0, subway.id, true))  //enter a subway
                //ways.add(Way(exit.position, exit.position, 0, null, true))  //exit a subway
            }
        }
        //add all the ways that are possible on the surface
        val subwayExits = getSubwayExitPairs()
        for ((_, exit) in subwayExits) {
            val waysStartingAtThatExit = mutableSetOf<Way>()
            subwayExits.forEach { (_, e) ->
                if (e.position != exit.position) {
                    /*waysStartingAtThatExit.add(
                        Way(
                            exit.position.copy(subwayId = null),
                            e.position,
                            exit.position.distanceTo(e.position),
                            null
                        )
                    )*/
                }
            }
            ways.addAll(waysStartingAtThatExit)
        }

        return ways
    }

    fun checkCollisions() {
        for (player in players) {
            for (mouse in mice.filter { !it.isDead && it.subway == null }) {
                if (player.intersects(mouse)) {
                    player.score += 1
                    mouse.isDead = true
                    break
                }
            }
        }
    }

    fun toDto(): GameBoardDto {
        return GameBoardDto(
            width = width,
            height = height,
            mice = mice.map { it.toDto() }.toMutableSet(),
            subways = subways.map { it.toDto() }.toMutableSet(),
            players = players.map { it.toDto() }.toMutableSet(),
            winningSubway = winningSubway!!.toDto()
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
        return winningSubway == other.winningSubway
    }

    override fun hashCode(): Int {
        var result = subways.hashCode()
        result = 31 * result + mice.hashCode()
        result = 31 * result + players.hashCode()
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + (winningSubway?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "GameBoard(subways=$subways, mice=$mice, players=$players, width=$width, height=$height, winningSubway=$winningSubway)"
    }
}
