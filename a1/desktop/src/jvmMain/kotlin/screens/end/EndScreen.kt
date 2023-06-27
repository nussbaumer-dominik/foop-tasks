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
import components.ScoreCard
import game.A1Game
import helper.toColor
import kotlinx.coroutines.launch
import models.GameState
import screens.A1Screen
import kotlin.system.exitProcess

class EndScreen(
    private val game: A1Game,
    private val restClient: A1RestClient,
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
                        text = "Scoreboard",
                        color = Color.Black,
                        style = MaterialTheme.typography.h3,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(
                        modifier = Modifier
                            .height(8.dp),
                    )
                    ScoreCard(
                        catScore = viewState.gameState?.gameBoard?.players?.sumOf { it.score }
                            ?: 0,
                        mouseScore = viewState.gameState?.gameBoard?.mice?.count { !it.isDead }
                            ?: 0,
                    )
                    Spacer(
                        modifier = Modifier
                            .height(30.dp),
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
                            val playerColor = player.color.toColor()

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
                    Column {
                        EndScreenButton(
                            text = "Restart game",
                            onClick = { screenScope.launch { restartGame() } }
                        )
                        EndScreenButton(
                            text = "Leave lobby",
                            onClick = { screenScope.launch { leaveLobby() } }
                        )
                        EndScreenButton(
                            text = "Exit application",
                            onClick = { exitProcess(0) }
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

    // TODO: fix this method -> should restart the game for the current lobby
    private suspend fun restartGame() {
        restClient.start()
    }

    // TODO: implement this method
    private suspend fun leaveLobby() {
        // -> should remove my current connection and go back to the start screen
        TODO("Not yet implemented")
    }

    data class ViewState(
        val gameState: GameState? = null,
    )
}

@Composable
fun EndScreenButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Gray,
            contentColor = Color.White,
        ),
        onClick = onClick,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.button,
        )
    }
}