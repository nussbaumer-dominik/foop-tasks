import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import at.ac.tuwien.foop.common.Message
import components.Board
import components.WelcomeScreen
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import kotlinx.serialization.json.Json

@Composable
fun App(messages: List<String>) {
    MaterialTheme {
        //WelcomeScreen(messages)
        Board()
    }
}

fun main() = application {
    var messages by remember { mutableStateOf(emptyList<String>()) }

    Window(onCloseRequest = ::exitApplication) {
        App(messages)
    }

    LaunchedEffect(true) {
        val client = HttpClient {
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
                val message = receiveDeserialized<Message>()
                messages = messages + message.text
            }
        }
        client.close()
    }
}
