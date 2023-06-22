package util

import androidx.compose.ui.awt.ComposeWindow

fun ComposeWindow.setContentSize(width: Int, height: Int) {
    this.pack()
    val insets = this.insets
    setSize(
        width + insets.left + insets.right,
        height + insets.top + insets.bottom
    )
}