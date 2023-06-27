package at.ac.tuwien.foop.domain

import at.ac.tuwien.foop.common.models.dtos.socket.HSLColorDto

data class HSLColor(
    val hue: Float,
    val saturation: Float,
    val lightness: Float,
) {
    companion object {
        fun fromDto(other: HSLColorDto): HSLColor {
            return HSLColor(
                hue = other.hue,
                saturation = other.saturation,
                lightness = other.lightness,
            )
        }
    }

    fun toDto(): HSLColorDto {
        return HSLColorDto(
            hue = hue,
            saturation = saturation,
            lightness = lightness,
        )
    }
}