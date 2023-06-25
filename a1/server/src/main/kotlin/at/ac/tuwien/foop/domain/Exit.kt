package at.ac.tuwien.foop.domain

import at.ac.tuwien.foop.common.models.dtos.socket.ExitDto

/**
 * The exit of a subway
 * */
class Exit(
    /**
     * The top left corner position of the exit
     * */
    override val position: Position,
    override val size: Size = Size(32, 32),
    val subwayId: String
) : Entity() {
    companion object {
        fun fromDto(dto: ExitDto): Exit {
            return Exit(
                position = Position.fromDto(dto.position),
                size = Size.fromDto(dto.size),
                subwayId = dto.subwayId,
            )
        }
    }

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

    fun toDto(): ExitDto {
        return ExitDto(
            position = position.toDto(),
            size = size.toDto(),
            subwayId = subwayId,
        )
    }
}