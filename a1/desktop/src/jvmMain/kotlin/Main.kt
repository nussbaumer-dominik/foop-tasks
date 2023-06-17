import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import at.ac.tuwien.foop.common.domain.GameBoard
import components.BoardView
import components.GameClient

@Composable
fun App(messages: List<String>, gameBoard: GameBoard) {
    MaterialTheme {
        //WelcomeScreen(messages)
        BoardView(gameBoard, ::onKeyEvent)
    }
}

fun onKeyEvent(e: KeyEvent) {
    println(e)
}

fun main() = application {
    var gameClient: GameClient
    val rows = 30
    val columns = 30
    val gameBoard by mutableStateOf(GameBoard(mutableSetOf(), mutableSetOf(), rows, columns))
    var messages by remember { mutableStateOf(emptyList<String>()) }
    val windowState = rememberWindowState(size = DpSize(960.dp, 960.dp))

    Window(
        onCloseRequest = ::exitApplication,
        resizable = true,
        state = windowState,
    ) {
        App(messages, gameBoard)
    }

    LaunchedEffect(true) {
        windowState.size = windowState.size.copy(
            width = (columns * Constants.TILE_SIZE).dp,
            height = (rows * Constants.TILE_SIZE + 37).dp
        )

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
