import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import at.ac.tuwien.foop.common.PrivateMessage
import at.ac.tuwien.foop.common.domain.GameBoardDto
import at.ac.tuwien.foop.common.domain.PlayerDto
import components.BoardView
import components.DebuggingOptions
import components.DebuggingOptionsView
import components.GameClient
import kotlinx.coroutines.runBlocking
import util.setContentSize
import util.toDirection

@Composable
fun App(gameBoardDto: GameBoardDto?) {
    var debuggingOptions by remember {
        mutableStateOf(DebuggingOptions())
    }
    MaterialTheme {
        Column {
            Row(
                modifier = Modifier
                    .size(height = Constants.TOP_NAV_HEIGHT.dp, width = Dp.Unspecified)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                DebuggingOptionsView(
                    debuggingOptions,
                    onChange = {
                        debuggingOptions = it
                    }
                )
            }
            Divider(color = Color.Black, thickness = 1.dp)
            Box(Modifier.fillMaxSize()) {
                if (gameBoardDto != null)
                    BoardView(gameBoardDto, debuggingOptions)
                else
                    Box(Modifier.fillMaxSize().align(Alignment.Center)) {
                        CircularProgressIndicator(Modifier.size(50.dp))
                    }
            }
        }
    }
}

fun main() = application {
    var composeWindow: ComposeWindow by mutableStateOf(ComposeWindow())
    var gameClient: GameClient? = null
    var gameBoardDto: GameBoardDto? by mutableStateOf(null)
    var playerDto: PlayerDto? by mutableStateOf(null)

    Window(
        create = {
            ComposeWindow().apply {
                composeWindow = this
                title = "Cat and Mouse"
                isResizable = true
                setSize(800, 600)
            }
        },
        onKeyEvent = {
            runBlocking {
                handleKeyEvent(it, gameClient, playerDto)
            }

            true
        },
        dispose = ComposeWindow::dispose,
    ) {
        App(gameBoardDto)
    }

    LaunchedEffect(true) {
        gameClient = GameClient(
            host = "127.0.0.1",
            port = 8080,
            onMapUpdate = {
                if (gameBoardDto == null) {
                    composeWindow.setContentSize(it.width, it.height + Constants.TOP_NAV_HEIGHT)
                }
                gameBoardDto = it
            },
            onStateUpdate = {
                gameBoardDto = it.map
            },
            onSetupInfo = {
                playerDto = it.playerDto
            },
        )

        gameClient!!.start()
        gameClient!!.dispose()
    }
}

private fun handleKeyEvent(
    keyEvent: KeyEvent,
    gameClient: GameClient?,
    playerDto: PlayerDto?
) {
    keyEvent.type
    val type = when (keyEvent.type) {
        KeyEventType.KeyDown -> PrivateMessage.MoveCommandType.MOVE
        KeyEventType.KeyUp -> PrivateMessage.MoveCommandType.STOP
        else -> return
    }

    val direction = keyEvent.key.toDirection()
    if (direction != null) {
        try {
            gameClient?.sendCommand(
                PrivateMessage.MoveCommand(
                    playerDto = playerDto,
                    direction = direction,
                    type = type,
                )
            )
        } catch (e: Exception) {
            println("Error sending command: $e")
        }
    }
}
