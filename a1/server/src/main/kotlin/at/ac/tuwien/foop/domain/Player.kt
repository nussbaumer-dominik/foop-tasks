package at.ac.tuwien.foop.domain

import at.ac.tuwien.foop.common.models.dtos.socket.PlayerDto
import at.ac.tuwien.foop.common.models.dtos.socket.PositionDto
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
    override val moveSize: Int = 4,
    var velocity: Velocity = Velocity(),
    var score: Int = 0,
    val color: HSLColor,
) : MovingEntity() {
    companion object {
        fun fromDto(dto: PlayerDto): Player {
            return Player(
                id = dto.id,
                position = Position.fromDto(dto.position),
                score = dto.score,
                color = HSLColor.fromDto(dto.color),
            )
        }
    }

    // TODO: maybe implement this in another way
    fun move(width: Int, height: Int) {
        var totalXVelocity = velocity.xr + velocity.xl
        var totalYVelocity = velocity.yu + velocity.yd

        if (totalXVelocity != 0 && totalYVelocity != 0) {
            totalXVelocity /= 2
            totalYVelocity /= 2
        }

        val newX = position.x + totalXVelocity
        val newY = position.y + totalYVelocity
        position = position.copy(
            x = if (newX < 0) 0 else if (newX > width - size.width) width - size.width else newX,
            y = if (newY < 0) 0 else if (newY > height - size.height) height - size.height else newY,
        )
    }

    fun toDto(): PlayerDto {
        return PlayerDto(
            id = id,
            username = "",
            position = PositionDto(0, 0),
            score = score,
            color = color.toDto(),
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Player

        if (id != other.id) return false
        if (size != other.size) return false
        if (position != other.position) return false
        return velocity == other.velocity
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + position.hashCode()
        result = 31 * result + velocity.hashCode()
        return result
    }

    override fun toString(): String {
        return "Player(id='$id', size=$size, position=$position, velocity=$velocity)"
    }

}
