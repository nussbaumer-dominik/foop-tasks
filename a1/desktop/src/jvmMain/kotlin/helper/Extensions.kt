package helper

import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import kotlin.math.abs

fun String.toColor(): Color =
    Color(("ff" + removePrefix("#").lowercase()).toLong(16))

fun ComposeWindow.setContentSize(width: Int, height: Int) {
    pack()
    setSize(
        width + insets.left + insets.right,
        height + insets.top + insets.bottom
    )
}

fun String.generateHSL(): Color {
    val hash = getHashOfString(this)
    val h = normalizeHash(hash, 0, 360)
    val s = normalizeHash(hash, 15, 90)
    val l = normalizeHash(hash, 10, 70)

    return Color.hsl(
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