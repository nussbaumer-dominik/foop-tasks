package screens.lobby

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.ac.tuwien.foop.common.client.A1RestClient
import at.ac.tuwien.foop.common.client.A1SocketClient
import at.ac.tuwien.foop.common.client.impl.SocketState
import at.ac.tuwien.foop.common.models.domain.socket.GameStatus
import at.ac.tuwien.foop.common.models.domain.socket.Player
import at.ac.tuwien.foop.common.models.domain.socket.PrivateMessage
import game.A1Game
import helper.generateHSL
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import screens.A1Screen
import screens.navigator.A1Navigator
import screens.navigator.NavigationDestination

class LobbyScreen(
    private val game: A1Game,
    private val restClient: A1RestClient,
    private val socketClient: A1SocketClient,
    private val navigator: A1Navigator,
) : A1Screen<LobbyScreen.ViewState>(ViewState()) {
    @Composable
    override fun render() {
        val screenScope = rememberCoroutineScope()
        val viewState by observeState().collectAsState()

        MaterialTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "Hello, ${viewState.currentPlayer?.username}",
                        color = Color.Black,
                        style = MaterialTheme.typography.h3,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(
                        modifier = Modifier
                            .height(5.dp),
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "All players in your current Lobby:",
                        color = Color.Gray,
                        style = MaterialTheme.typography.h5.copy(fontSize = 18.sp),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(
                        modifier = Modifier
                            .height(20.dp),
                    )
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth(),
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(
                            items = viewState.players.toList(),
                            key = { it.id }
                        ) { player ->
                            val playerColor = player.username.generateHSL()
                            Card(
                                backgroundColor = playerColor,
                                elevation = 0.dp,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    text = player.username,
                                    color = if (playerColor.luminance() < 0.6) Color.White else Color.Black,
                                    style = MaterialTheme.typography.body1,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(30.dp),
                    )
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Black,
                            contentColor = Color.White,
                        ),
                        onClick = { screenScope.launch { startGame() } },
                    ) {
                        Text(
                            text = "Start game!",
                            style = MaterialTheme.typography.button,
                        )
                    }
                }
            }
        }

        LaunchedEffect(true) {
            launch { observeGameState() }
            launch { observeSocketState() }
            launch { observeCurrentPlayer() }
        }
    }

    private suspend fun observeCurrentPlayer() {
        game.observeCurrentPlayer()
            .collectLatest { currentPlayer ->
                updateState { state -> state.copy(currentPlayer = currentPlayer) }
            }
    }

    private suspend fun observeGameState() {
        game.observeGameState()
            .filterNotNull()
            .collectLatest { gameState ->
                updateState { state -> state.copy(players = gameState.gameBoard.players) }
                if (gameState.gameStatus == GameStatus.RUNNING) {
                    navigator.navigate(NavigationDestination.GAME)
                }
            }
    }

    private suspend fun observeSocketState() {
        socketClient.observeState()
            .collectLatest { state ->
                if (state == SocketState.OPEN) {
                    val currentPlayerId = game.getCurrentPlayerId()
                    if (currentPlayerId != null) {
                        socketClient.join(PrivateMessage.JoinRequest(id = currentPlayerId))
                    }
                }
            }
    }

    private suspend fun startGame() {
        restClient.start()
    }

    data class ViewState(
        val currentPlayer: Player? = null,
        val players: Set<Player> = emptySet(),
    )
}
