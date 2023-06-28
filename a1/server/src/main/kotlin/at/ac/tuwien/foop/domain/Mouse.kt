package at.ac.tuwien.foop.domain

import at.ac.tuwien.foop.common.models.dtos.socket.MouseDto
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
    var isDead: Boolean = false,
    var subway: Subway?,
    val strategy: MouseStrategy = MouseRandomStrategy(),
    var catsPositions: List<Position> = mutableListOf(),
    var targetEntity: Entity? = null,
    var targetSubwayExit: Exit? = null,
) : MovingEntity() {
    companion object {
        fun fromDto(dto: MouseDto): Mouse {
            return Mouse(
                id = dto.id,
                position = Position.fromDto(dto.position),
                isDead = dto.isDead,
                size = Size.fromDto(dto.size),
                subway = dto.subway?.let { Subway.fromDto(it) },
            )
        }
    }

    fun move(gameBoard: GameBoard) {
        position = strategy.newPosition(this, gameBoard)
    }

    fun toDto(): MouseDto {
        return MouseDto(
            id = id,
            position = position.toDto(),
            isDead = isDead,
            subway = subway?.toDto(),
            size = size.toDto(),
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
