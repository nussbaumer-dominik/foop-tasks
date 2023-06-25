package at.ac.tuwien.foop.domain

abstract class Entity {
    abstract val position: Position
    abstract val size: Size

    fun intersects(other: Entity): Boolean {
        return this.position.x < other.position.x + other.size.width &&
                this.position.x + this.size.width > other.position.x &&
                this.position.y < other.position.y + other.size.height &&
                this.position.y + this.size.height > other.position.y
    }

    private fun center() = Size(size.width / 2, size.height / 2)

    fun centerIntersects(other: Entity): Boolean {
        val center = center()
        return this.position.x + center.width < other.position.x + other.size.width &&
                this.position.x + center.width > other.position.x &&
                this.position.y + center.height < other.position.y + other.size.height &&
                this.position.y + center.height > other.position.y
    }
}
