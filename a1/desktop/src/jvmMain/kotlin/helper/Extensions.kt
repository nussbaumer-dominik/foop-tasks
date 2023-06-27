package helper

import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import at.ac.tuwien.foop.common.models.domain.socket.HSLColor

fun ComposeWindow.setContentSize(width: Int, height: Int) {
    pack()
    setSize(
        width + insets.left + insets.right,
        height + insets.top + insets.bottom
    )
}

fun HSLColor.toColor(): Color = Color.hsl(
    hue = hue,
    saturation = saturation,
    lightness = lightness
)