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
fun App(messages: List<String>, tileSize: Int = 32, rows: Int, columns: Int) {
    MaterialTheme {
        //WelcomeScreen(messages)
        BoardView(tileSize = tileSize, rows = rows, columns = columns, ::onKeyEvent)
    }
}

fun onKeyEvent(e: KeyEvent) {
    println(e)
}

fun main() = application {
    var gameClient: GameClient
    var gameState by mutableStateOf(GameBoard(mutableSetOf(), mutableSetOf(), 32, 32))
    var messages by remember { mutableStateOf(emptyList<String>()) }
    val tileSize = 32
    val rows = 30
    val columns = 30
    val windowState = rememberWindowState(size = DpSize(800.dp, 800.dp))

    Window(
        onCloseRequest = ::exitApplication,
        resizable = true,
        state = windowState,
    ) {
        App(messages, tileSize, rows, columns)
    }

    windowState.size = windowState.size.copy(
        width = (rows * tileSize - 16).dp,
        height = (columns * tileSize + 7).dp
    )
    LaunchedEffect(true) {
        windowState.size = windowState.size.copy(
            width = (rows * tileSize - 16).dp,
            height = (columns * tileSize + 7).dp
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
