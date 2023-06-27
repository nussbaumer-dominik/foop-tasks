package helper

import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

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
        // TODO: add dynamic resizing back in to get the correct size for the game board once the first map is received
        create = {
            ComposeWindow().apply {
                title = "Cat and Mouse"
                isResizable = true
                setContentSize(800, 850)
            }
        },
        onKeyEvent = onKeyEvent,
        dispose = ComposeWindow::dispose,
        content = content,
    )
}
