package at.ac.tuwien.foop.domain

import at.ac.tuwien.foop.common.domain.ExitDto

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
                position = Position.fromDto(dto.positionDto),
                size = Size.fromDto(dto.sizeDto),
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
            positionDto = position.toDto(),
            sizeDto = size.toDto(),
            subwayId = subwayId,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Exit

        if (position != other.position) return false
        if (size != other.size) return false
        return subwayId == other.subwayId
    }

    override fun hashCode(): Int {
        var result = position.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + subwayId.hashCode()
        return result
    }

    override fun toString(): String {
        return "Exit(position=$position, size=$size, subwayId='$subwayId')"
    }

}
