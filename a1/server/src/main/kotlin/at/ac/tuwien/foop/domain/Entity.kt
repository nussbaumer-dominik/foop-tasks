package at.ac.tuwien.foop.domain

abstract class Entity {
    abstract val position: Position
    abstract val size: Size
    abstract val moveSize: Int

    fun intersects(other: Entity): Boolean {
        return this.position.x < other.position.x + other.size.width &&
                this.position.x + this.size.width > other.position.x &&
                this.position.y < other.position.y + other.size.height &&
                this.position.y + this.size.height > other.position.y
    }
}