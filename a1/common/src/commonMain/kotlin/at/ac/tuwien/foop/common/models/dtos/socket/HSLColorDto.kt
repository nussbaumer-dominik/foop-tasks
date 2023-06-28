package at.ac.tuwien.foop.common.models.dtos.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HSLColorDto(
    @SerialName("hue")
    val hue: Float,
    @SerialName("saturation")
    val saturation: Float,
    @SerialName("lightness")
    val lightness: Float
)