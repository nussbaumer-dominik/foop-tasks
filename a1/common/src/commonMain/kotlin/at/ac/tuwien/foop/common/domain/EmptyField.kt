package at.ac.tuwien.foop.common.domain

class EmptyField: Field {
    override fun toChar(): Char {
        return ' '
    }
}
