package at.ac.tuwien.foop.domain

class ConcreteMovingEntity(
    override var position: Position,
    override val size: Size,
    override val moveSize: Int,
) : MovingEntity() {

    constructor(entity: MovingEntity) : this(entity.position.copy(), entity.size.copy(), entity.moveSize)

    fun copyWith(position: Position? = null, size: Size? = null, moveSize: Int? = null): ConcreteMovingEntity {
        return ConcreteMovingEntity(
            position = position?.copy() ?: this.position.copy(),
            size = size?.copy() ?: this.size.copy(),
            moveSize = moveSize ?: this.moveSize,
        )
    }
}
