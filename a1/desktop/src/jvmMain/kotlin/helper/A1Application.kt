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
