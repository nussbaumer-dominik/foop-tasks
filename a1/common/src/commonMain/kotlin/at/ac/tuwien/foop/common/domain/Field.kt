package at.ac.tuwien.foop.common.domain

sealed interface Field {
    /*abstract*/ val position: Position
    /*abstract*/ val size: Size

    /*fun intersects(other: Field): Boolean {
        return other.position.x in position.x until (position.x + size.width) &&
                other.position.y in position.y until (position.y + size.height) ||
                position.x in other.position.x until (other.position.x + other.size.width) &&
                position.y in other.position.y until (other.position.y + other.size.height)
    }*/

    /*abstract*/ fun toChar(): Char
}
