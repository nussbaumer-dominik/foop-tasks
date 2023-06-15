package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The exit of a subway
 * */
@Serializable
data class Exit(
    /**
     * The starting position of the exit
     * */
    @SerialName("position")
    val position: Position,
): Field {
    fun getDistancesToOtherExits(gameBoard: GameBoard): List<Pair<Int, Exit>> {
        val distances = mutableListOf<Pair<Int, Exit>>()
        for (s in gameBoard.subways) {
            for (e in s.exits) {
                distances.add(Pair(position.distanceTo(e.position), e))
            }
        }
        distances.sortBy { it.first }
        return distances
    }

    override fun toChar(): Char {
        return '0'
    }
}
