package at.ac.tuwien.foop.domain

class ConcreteEntity(
    override var position: Position,
    override val size: Size
) : Entity()