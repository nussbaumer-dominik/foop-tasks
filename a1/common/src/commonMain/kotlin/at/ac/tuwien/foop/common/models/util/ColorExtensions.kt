package at.ac.tuwien.foop.common.models.util

import at.ac.tuwien.foop.common.models.domain.socket.HSLColor
import kotlin.math.abs

fun String.generateHSL(): HSLColor {
    val hash = getHashOfString(this)
    val h = normalizeHash(hash, 0, 360)
    val s = normalizeHash(hash, 15, 90)
    val l = normalizeHash(hash, 10, 70)

    return HSLColor(
        hue = h.toFloat(),
        saturation = s.toFloat() / 100,
        lightness = l.toFloat() / 100
    )
}

private fun normalizeHash(hash: Int, min: Int, max: Int): Int {
    return (hash % (max - min)) + min
}

private fun getHashOfString(str: String) = abs(str.fold(0) { hash, char ->
    char.code + ((hash shl 5) - hash)
})