import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import components.BoardView

@Composable
fun App(messages: List<String>, tileSize: Int = 32, rows: Int, columns: Int) {
    MaterialTheme {
        //WelcomeScreen(messages)
        BoardView(tileSize = tileSize, rows = rows, columns = columns)
    }
}

fun main() = application {
    var messages by remember { mutableStateOf(emptyList<String>()) }
    val tileSize = 32
    val windowState = rememberWindowState(size = DpSize((32 * tileSize - 16).dp, (32 * tileSize + 7).dp))
    Window(
        onCloseRequest = ::exitApplication,
        resizable = true,
        state = windowState,
    ) {
        App(messages, tileSize, 30, 30)
    }

    LaunchedEffect(true) {
        windowState.size = DpSize((32 * tileSize).dp, (32 * tileSize + 29).dp)
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
