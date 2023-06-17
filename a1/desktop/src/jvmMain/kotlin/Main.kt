import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.input.key.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import at.ac.tuwien.foop.common.domain.*
import at.ac.tuwien.foop.common.domain.Direction
import components.BoardView
import components.GameClient

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
        /*if (gameBoard != null)
            App(gameBoard!!)*/
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

    LaunchedEffect(true) {
        /*gameClient = GameClient("127.0.0.1", 8080) {
            // TODO: rework into its own callback
            if (gameBoard == null && !firstGameBoard) {
                firstGameBoard = true
                println(it.columns)
                println(it.rows)
                composeWindow.setContentSize(
                    it.columns * Constants.TILE_SIZE,
                    it.rows * Constants.TILE_SIZE
                )
            }
            gameBoard = it
        }

        gameClient.receive()

        gameClient.dispose()*/
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
