package at.ac.tuwien.foop.domain

import at.ac.tuwien.foop.common.domain.MouseDto
import at.ac.tuwien.foop.domain.mouseStrategy.MouseRandomStrategy
import at.ac.tuwien.foop.domain.mouseStrategy.MouseStrategy
import java.util.*

class Mouse(
    /**
     * The unique id of a mouse which is assigned when it is spawned in
     * */
    val id: String = UUID.randomUUID().toString(),
    /**
     * The current position of this mouse on the map relative to the top left corner
     * */
    override var position: Position,
    override val size: Size = Size(32, 32),
    override val moveSize: Int = 2,
    var subway: Subway?,
    val strategy: MouseStrategy = MouseRandomStrategy(),
    var catsPositions: List<Position> = mutableListOf(),
    var targetEntity: Entity? = null,
    var targetSubwayExitEntity: Entity? = null,
) : MovingEntity() {
    companion object {
        fun fromDto(dto: MouseDto): Mouse {
            return Mouse(
                id = dto.id,
                position = Position.fromDto(dto.positionDto),
                size = Size.fromDto(dto.sizeDto),
                subway = dto.subwayDto?.let { Subway.fromDto(it) },
            )
        }
    }

    fun move(gameBoard: GameBoard) {
        val newPosition = strategy.newPosition(this, gameBoard)
        /*val field = gameBoard.getFieldAtPosition(newPosition)
        if (field is Exit) {
            subway = gameBoard.subways.first { it.id == field.subwayId }
        }*/

        position = newPosition
    }

    fun toDto(): MouseDto {
        return MouseDto(
            id = id,
            positionDto = position.toDto(),
            sizeDto = size.toDto(),
            subwayDto = subway?.toDto(),
        )
    }

    fun toEntity(): ConcreteMovingEntity {
        return ConcreteMovingEntity(
            this
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Mouse

        if (id != other.id) return false
        if (position != other.position) return false
        if (size != other.size) return false
        return subway != other.subway
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + position.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + (subway?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Mouse(id='$id', position=$position, size=$size, subway=$subway, strategy=$strategy, catsPositions=$catsPositions, targetPosition=$targetEntity)"
    }


}
