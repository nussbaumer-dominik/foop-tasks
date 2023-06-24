package at.ac.tuwien.foop.domain

class ConcreteEntity(
    override var position: Position,
    override val size: Size
) : Entity() {

    constructor(entity: Entity) : this(entity.position, entity.size)
    fun copyWith(position: Position?, size: Size? = null): Entity {
        return ConcreteEntity(
            position = position ?: this.position,
            size = size ?: this.size
        )
    }
}
