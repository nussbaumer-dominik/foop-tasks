import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.input.key.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import at.ac.tuwien.foop.common.domain.Direction
import at.ac.tuwien.foop.common.domain.GameBoard
import components.BoardView
import components.GameClient

@Composable
fun App(gameBoard: GameBoard) {
    MaterialTheme {
        BoardView(gameBoard)
    }
}

fun main() = application {
    var composeWindow: ComposeWindow by mutableStateOf(ComposeWindow())
    var gameClient: GameClient
    var gameBoard: GameBoard? by mutableStateOf(null)
    var firstGameBoard = false

    Window(
        create = {
            ComposeWindow().apply {
                composeWindow = this
                title = "Cat and Mouse"
                isResizable = false
                setSize(800, 600)
            }
        },
        onKeyEvent = {
            handleKeyEvent(it)
            true
        },
        dispose = ComposeWindow::dispose,
    ) {
        if (gameBoard != null)
            App(gameBoard!!)
    }

    LaunchedEffect(true) {
        gameClient = GameClient("127.0.0.1", 8080) {
            /*if (gameBoard == null && !firstGameBoard) {
                firstGameBoard = true
                println(it.columns)
                println(it.rows)
                composeWindow.setContentSize(
                    it.columns * Constants.TILE_SIZE,
                    it.rows * Constants.TILE_SIZE
                )
            }*/
            gameBoard = it
        }

        gameClient.receive()

        gameClient.dispose()
    }
}

fun ComposeWindow.setContentSize(width: Int, height: Int) {
    this.pack()
    val insets = this.insets
    setSize(
        width + insets.left + insets.right,
        height + insets.top + insets.bottom
    )
}

private fun handleKeyEvent(keyEvent: KeyEvent): Direction? {
    if (keyEvent.type != KeyEventType.KeyDown) return null
    val direction = keyEvent.key.toDirection()
    println(direction)
    return direction
}

@OptIn(ExperimentalComposeUiApi::class)
fun Key.toDirection(): Direction? {
    return when (this) {
        Key.DirectionUp -> Direction.UP
        Key.DirectionDown -> Direction.DOWN
        Key.DirectionLeft -> Direction.LEFT
        Key.DirectionRight -> Direction.RIGHT
        else -> null
    }
}
