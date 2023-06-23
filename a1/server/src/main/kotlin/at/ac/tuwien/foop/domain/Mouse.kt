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
    override val position: Position,
    override val size: Size = Size(32, 32),
    var subway: Subway?,
    val strategy: MouseStrategy = MouseRandomStrategy(),
    var catsPositions: List<Position> = mutableListOf(),
    var targetPosition: Position? = null,
) : Entity() {
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
        val field = gameBoard.getFieldAtPosition(newPosition)
        if (field is Exit) {
            subway = gameBoard.subways.first { it.id == field.subwayId }
        }

        position.x = newPosition.x
        position.y = newPosition.y
    }

    fun toDto(): MouseDto {
        return MouseDto(
            id = id,
            positionDto = position.toDto(),
            sizeDto = size.toDto(),
            subwayDto = subway?.toDto(),
        )
    }
}