package at.ac.tuwien.foop.domain

import at.ac.tuwien.foop.common.domain.PlayerDto
import java.util.*

/**
 * A cat controlled by a player
 * */
class Player(
    /**
     * The unique id of the play assigned when they join the game
     * */
    val id: String = UUID.randomUUID().toString(),
    override val size: Size = Size(32, 32),
    /**
     * The current position of the player on the map relative to the top left corner
     * */
    override var position: Position,
    /**
     * The unique color assigned to this player to distinguish it form others
     * */
    val color: String,
    var velocity: Velocity = Velocity(),
) : Entity() {
    companion object {
        fun fromDto(dto: PlayerDto): Player {
            return Player(
                id = dto.id,
                position = Position.fromDto(dto.positionDto),
                size = Size.fromDto(dto.sizeDto),
                color = dto.color,
            )
        }
    }

    // TODO: implement this in another way
    fun move(width: Int, height: Int) {
        val newX = position.x + velocity.xr + velocity.xl
        val newY = position.y + velocity.yu + velocity.yd
        position = position.copyWith(
            x = if (newX < 0) 0 else if (newX > width - size.width) width - size.width else newX,
            y = if (newY < 0) 0 else if (newY > height - size.height) height - size.height else newY,
        )
    }

    fun toDto(): PlayerDto {
        return PlayerDto(
            id = id,
            positionDto = position.toDto(),
            sizeDto = size.toDto(),
            color = color,
        )
    }
}