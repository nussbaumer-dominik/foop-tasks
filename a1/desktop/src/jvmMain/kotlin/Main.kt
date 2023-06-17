import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.input.key.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import at.ac.tuwien.foop.common.domain.Direction
import at.ac.tuwien.foop.common.domain.GameBoard
import at.ac.tuwien.foop.common.util.GameBoardGenerator
import components.BoardView
import components.GameClient

@Composable
fun App(messages: List<String>, gameBoard: GameBoard) {
    MaterialTheme {
        //WelcomeScreen(messages)
        BoardView(gameBoard)
    }
}

fun main() = application {
    var composeWindow: ComposeWindow by mutableStateOf(ComposeWindow())
    var gameClient: GameClient
    val rows = 30
    val columns = 30
    //val gameBoard by mutableStateOf(GameBoard(mutableSetOf(), mutableSetOf(), rows, columns))
    val gameBoard by mutableStateOf(
        GameBoardGenerator().generateGameBoard(
            rows,
            columns,
            numberOfSubways = 10,
            numberOfMice = 50
        )
    )
    var messages by remember { mutableStateOf(emptyList<String>()) }

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
        App(messages, gameBoard)
    }

    LaunchedEffect(true) {
        composeWindow.setContentSize(columns * Constants.TILE_SIZE, rows * Constants.TILE_SIZE)

        /*gameClient = GameClient("127.0.0.1", 8080) {
            gameState = it
        }

        while (true) {
            gameClient.receive()
        }
        gameClient.dispose()*/
        /*val client = HttpClient {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
        }
        client.webSocket(
            method = HttpMethod.Get,
            host = "127.0.0.1",
            port = 8080,
            path = "/ws"
        ) {
            while (true) {
                val message = incoming.receive() as? Frame.Text ?: continue
                messages = messages + message.readText()
            }
        }
        client.close()*/
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
