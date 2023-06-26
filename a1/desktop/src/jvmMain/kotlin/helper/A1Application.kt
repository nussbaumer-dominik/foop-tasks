package helper

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Color

fun a1Application(
    onKeyEvent: (event: KeyEvent) -> Boolean,
    content: @Composable FrameWindowScope.() -> Unit,
) {
    application {
        A1Window(onKeyEvent = onKeyEvent) {
            content()
        }
    }
}

@Composable
fun A1Window(
    onKeyEvent: (event: KeyEvent) -> Boolean,
    content: @Composable FrameWindowScope.() -> Unit,
) {
    Window(
        // TODO: add dynamic resizing back in to get the correct size for the game board
        create = {
            ComposeWindow().apply {
                title = "Cat and Mouse"
                isResizable = true
                setSize(800, 900)
            }
        },
        onKeyEvent = onKeyEvent,
        dispose = ComposeWindow::dispose,
        content = content,
    )
}
