import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import at.ac.tuwien.foop.common.client.A1RestClient
import at.ac.tuwien.foop.common.client.A1SocketClient
import game.A1Game
import screens.end.EndScreen
import screens.game.GameScreen
import screens.lobby.LobbyScreen
import screens.navigator.A1Navigator
import screens.navigator.NavigationDestination
import screens.start.StartScreen

@Composable
fun App(
    game: A1Game,
    navigator: A1Navigator,
    restClient: A1RestClient,
    socketClient: A1SocketClient,
) {
    val currentDestination by navigator.observeDestination().collectAsState()

    when (currentDestination) {
        NavigationDestination.START ->
            StartScreen(game, restClient, navigator).render()

        NavigationDestination.LOBBY ->
            LobbyScreen(game, restClient, socketClient, navigator).render()

        NavigationDestination.GAME ->
            GameScreen(game, navigator).render()

        NavigationDestination.END ->
            EndScreen(game, restClient).render()
    }
}
