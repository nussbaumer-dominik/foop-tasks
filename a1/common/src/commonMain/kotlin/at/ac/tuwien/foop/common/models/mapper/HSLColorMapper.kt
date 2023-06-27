package at.ac.tuwien.foop.common.models.mapper

import at.ac.tuwien.foop.common.models.domain.socket.HSLColor
import at.ac.tuwien.foop.common.models.dtos.socket.HSLColorDto

fun HSLColor.mapToDto(): HSLColorDto = HSLColorDto(
    hue = hue,
    saturation = saturation,
    lightness = lightness,
)

fun HSLColorDto.map(): HSLColor = HSLColor(
    hue = hue,
    saturation = saturation,
    lightness = lightness,
)
