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
    private var grid: Array<Array<Entity>>? = null
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
}
