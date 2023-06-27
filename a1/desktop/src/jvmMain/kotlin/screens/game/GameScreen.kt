package screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import at.ac.tuwien.foop.common.models.domain.socket.GameStatus
import components.BoardView
import components.DebuggingOptions
import components.DebuggingOptionsView
import game.A1Game
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import models.GameState
import screens.A1Screen
import screens.navigator.A1Navigator
import screens.navigator.NavigationDestination

class GameScreen(
    private val game: A1Game,
    private val navigator: A1Navigator,
) : A1Screen<GameScreen.ViewState>(ViewState()) {
    @Composable
    override fun render() {
        var debuggingOptions by remember { mutableStateOf(DebuggingOptions()) }

        val viewState by observeState().collectAsState()

        MaterialTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    DebuggingOptionsView(
                        debuggingOptions,
                        onChange = {
                            debuggingOptions = it
                        }
                    )
                    Divider(
                        color = Color.Black,
                        thickness = 1.dp,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val gameState = viewState.gameState
                        if (gameState != null) {
                            BoardView(gameState.gameBoard, debuggingOptions)
                        } else {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(50.dp),
                            )
                        }
                    }
                }
            }
        }

        LaunchedEffect(true) {
            launch { observeGameState() }
        }
    }

    private suspend fun observeGameState() {
        game.observeGameState()
            .filterNotNull()
            .collectLatest { gameState ->
                updateState { state -> state.copy(gameState = gameState) }
                if (gameState.gameStatus != GameStatus.RUNNING && gameState.gameStatus != GameStatus.WAITING) {
                    navigator.navigate(NavigationDestination.END)
                }
            }
    }

    data class ViewState(
        val gameState: GameState? = null,
    )
}
