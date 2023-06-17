import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import at.ac.tuwien.foop.common.PrivateMessage
import at.ac.tuwien.foop.common.domain.*
import components.BoardView
import components.GameClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Preview
@Composable
fun AppPreview() {
    App(
        GameBoard(
            subways = mutableSetOf(
                Subway(
                    "0",
                    mutableSetOf(
                        Exit(Position(0, 0), "0"),
                        Exit(Position(10, 10), "1"),
                    )
                )
            ),
            mice = mutableSetOf(
                Mouse("0", position = Position(1, 1), subway = null),
                Mouse("1", position = Position(11, 11), subway = null),
            ),
            cats = mutableSetOf(
                Player("0", color = "red", position = Position(2, 2)),
                Player("1", color = "blue", position = Position(12, 12)),
            ),
            rows = 20,
            columns = 20
        )
    )
}

@Composable
fun App(gameBoard: GameBoard) {
    MaterialTheme {
        BoardView(gameBoard)
    }
}

fun main() = application {
    var composeWindow: ComposeWindow by mutableStateOf(ComposeWindow())
    var gameClient: GameClient? = null
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
        onKeyEvent = { keyEvent ->
            val direction = if (keyEvent.type != KeyEventType.KeyDown) null else keyEvent.key.toDirection()
            if (direction != null) {
                val command = PrivateMessage.MoveCommand(direction)
                // TODO: solve without using GlobalScope
                GlobalScope.launch { gameClient?.send(command) }
            }

            true
        },
        dispose = ComposeWindow::dispose,
    ) {
        if (gameBoard != null)
            App(gameBoard!!)
        else CircularProgressIndicator()
    }

    LaunchedEffect(true) {
        gameClient = GameClient(
            host = "127.0.0.1",
            port = 8080,
            onMapUpdate = {
                gameBoard = it
                if (!firstGameBoard) {
                    firstGameBoard = true
                    composeWindow.setContentSize(
                        it.columns * Constants.TILE_SIZE,
                        it.rows * Constants.TILE_SIZE
                    )
                }
            },
            onStateUpdate = {
                println("State update: $it")
                gameBoard = it.map
            }
        )

        gameClient!!.receive()
        gameClient!!.dispose()
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
