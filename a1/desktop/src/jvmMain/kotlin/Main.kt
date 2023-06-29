import game.impl.getGame
import helper.A1KeyHandler
import helper.a1Application
import helper.handleMessage
import helper.withResources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import screens.navigator.impl.getNavigator

fun main() {
    val game = getGame()
    val navigator = getNavigator()
    val host = System.getenv("HOST") ?: "localhost"

    withResources(
        restUrl = "http://${host}:8080",
        socketUrl = "ws://${host}:8080/ws",
        onSocketMessage = { message -> handleMessage(game, message) },
    ) { restClient, socketClient ->
        launch(Dispatchers.IO) {
            socketClient.connect()
        }

        a1Application(
            onKeyEvent = { event ->
                launch(Dispatchers.Default) {
                    A1KeyHandler(game, socketClient).handleKeyEvent(event)
                }
                true
            }
        ) {
            App(
                game = game,
                navigator = navigator,
                restClient = restClient,
                socketClient = socketClient,
            )
        }
    }
}
