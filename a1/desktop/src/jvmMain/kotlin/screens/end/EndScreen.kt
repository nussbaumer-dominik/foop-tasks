package screens.end

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import at.ac.tuwien.foop.common.client.A1RestClient
import at.ac.tuwien.foop.common.models.domain.socket.GameStatus
import game.A1Game
import helper.generateHSL
import kotlinx.coroutines.launch
import models.GameState
import screens.A1Screen
import screens.navigator.A1Navigator
import screens.navigator.NavigationDestination
import kotlin.system.exitProcess

class EndScreen(
    private val game: A1Game,
    private val restClient: A1RestClient,
    private val navigator: A1Navigator,
) : A1Screen<EndScreen.ViewState>(ViewState()) {
    @Composable
    override fun render() {
        val screenScope = rememberCoroutineScope()
        val viewState by observeState().collectAsState()

        getCurrentGameState()

        MaterialTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.6f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = gameStatusMessage(viewState.gameState?.gameStatus),
                        color = Color.Black,
                        style = MaterialTheme.typography.h3,
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
                            items = viewState.gameState?.gameBoard?.players?.toList()
                                ?.sortedByDescending { it.score }
                                ?: emptyList(),
                            key = { it.id }
                        ) { player ->
                            val playerColor = player.username.generateHSL()

                            Card(
                                backgroundColor = playerColor,
                                elevation = 0.dp,
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .padding(16.dp),
                                        text = player.username,
                                        color = if (playerColor.luminance() < 0.6) Color.White else Color.Black,
                                        style = MaterialTheme.typography.body1,
                                        textAlign = TextAlign.Left,
                                    )
                                    Text(
                                        modifier = Modifier
                                            .padding(16.dp),
                                        text = "${player.score}",
                                        color = if (playerColor.luminance() < 0.6) Color.White else Color.Black,
                                        style = MaterialTheme.typography.body1,
                                        textAlign = TextAlign.Right,
                                    )
                                }
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
                        onClick = { screenScope.launch { restartGame() } },
                    ) {
                        Text(
                            text = "Play again",
                            style = MaterialTheme.typography.button,
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(5.dp),
                    )
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Gray,
                            contentColor = Color.White,
                        ),
                        onClick = { exitProcess(0) },
                    ) {
                        Text(
                            text = "Exit application",
                            style = MaterialTheme.typography.button,
                        )
                    }
                }
            }
        }
    }

    private fun getCurrentGameState() {
        updateState { state ->
            state.copy(gameState = game.getCurrentGameState())
        }
    }

    private suspend fun restartGame() {
        val playerId = game.getCurrentPlayerId()
        println("Play again pressed for player $playerId")
        navigator.navigate(NavigationDestination.LOBBY)
        TODO("not implemented yet")
    }

    private fun gameStatusMessage(gameStatus: GameStatus?) = when (gameStatus) {
        GameStatus.CATS_WON -> "You won!"
        GameStatus.MICE_WON -> "You lost!"
        else -> "Unknown game status"
    }

    data class ViewState(
        val gameState: GameState? = null,
    )
}