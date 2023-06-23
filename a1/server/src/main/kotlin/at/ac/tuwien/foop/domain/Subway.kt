package at.ac.tuwien.foop.domain

import at.ac.tuwien.foop.common.domain.SubwayDto
import java.util.*

class Subway(
    /**
     * The unique id of a subway which is assigned when it is spawned
     */
    val id: String = UUID.randomUUID().toString(),
    /**
     * The exits that exist on this subway
     * */
    val exits: MutableSet<Exit> = mutableSetOf(),
) {
    companion object {
        fun fromDto(dto: SubwayDto): Subway {
            return Subway(
                id = dto.id,
                exits = dto.exitDtos.map { Exit.fromDto(it) }.toMutableSet(),
            )
        }
    }

    fun addExit(exit: Exit): Boolean {
        if (containsExit(exit)) {
            return false
        }

        exits.add(exit)
        return true
    }

    private fun containsExit(exit: Exit): Boolean {
        return exits.stream().anyMatch { e -> e.position == exit.position }
    }

    fun toDto(): SubwayDto {
        return SubwayDto(
            id = id,
            exitDtos = exits.map { it.toDto() }.toMutableSet(),
        )
    }
}