package at.ac.tuwien.foop.common.domain

class EmptyField(
    override val position: Position,
    override val size: Size
) : Field {
    override fun toChar(): Char {
        return ' '
    }
}
