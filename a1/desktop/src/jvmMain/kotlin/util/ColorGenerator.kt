package util

import androidx.compose.ui.graphics.Color
import kotlin.math.abs

class ColorGenerator {
    companion object {
        private val hRange = intArrayOf(0, 360)
        private val sRange = intArrayOf(15, 100)
        private val lRange = intArrayOf(10, 100)

        fun generateHSL(str: String): Color {
            val hash = getHashOfString(str)
            val h = normalizeHash(hash, hRange[0], hRange[1])
            val s = normalizeHash(hash, sRange[0], sRange[1])
            val l = normalizeHash(hash, lRange[0], lRange[1])

            return Color.hsl(
                hue = h.toFloat(),
                saturation = s.toFloat() / 100,
                lightness = l.toFloat() / 100
            )
        }
    }
}

fun normalizeHash(hash: Int, min: Int, max: Int): Int {
    return (hash % (max - min)) + min
}

fun getHashOfString(str: String) = abs(str.fold(0) { hash, char ->
    char.code + ((hash shl 5) - hash)
})