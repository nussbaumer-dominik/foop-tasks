package at.ac.tuwien.foop.domain

import at.ac.tuwien.foop.common.domain.SizeDto

data class Size(
    val width: Int,
    val height: Int
) {
    companion object {
        fun fromDto(sizeDto: SizeDto): Size {
            return Size(
                width = sizeDto.width,
                height = sizeDto.height
            )
        }
    }

    fun toDto(): SizeDto {
        return SizeDto(
            width = width,
            height = height
        )
    }
}