import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import at.ac.tuwien.foop.common.PrivateMessage
import at.ac.tuwien.foop.common.domain.*
import components.BoardView
import components.DebuggingOptions
import components.GameClient
import components.primitives.OptionTile
import kotlinx.coroutines.runBlocking
import util.setContentSize
import util.toDirection

@Preview
@Composable
fun AppPreview() {
    App(
        GameBoard(
            subways = mutableSetOf(
                Subway(
                    "0",
                    mutableSetOf(
                        Exit(Position(0, 0), "0"),
                        Exit(Position(10, 10), "1"),
                    )
                )
            ),
            mice = mutableSetOf(
                Mouse("0", position = Position(1, 1), subway = null),
                Mouse("1", position = Position(11, 11), subway = null),
            ),
            cats = mutableSetOf(
                Player("0", color = "red", position = Position(2, 2)),
                Player("1", color = "blue", position = Position(12, 12)),
            ),
            rows = 20,
            columns = 20
        )
    )
}

@Composable
fun App(gameBoard: GameBoard?) {
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
                OptionTile(
                    checked = debuggingOptions.showEmptyTiles,
                    text = "Show empty tiles",
                    onCheckedChange = { checked ->
                        debuggingOptions = debuggingOptions.copy(showEmptyTiles = checked)
                    }
                )
                OptionTile(
                    checked = debuggingOptions.showMouseTrace,
                    text = "Show mouse trace",
                    onCheckedChange = { checked ->
                        debuggingOptions = debuggingOptions.copy(showMouseTrace = checked)
                    }
                )
                OptionTile(
                    checked = debuggingOptions.showColoredSubways,
                    text = "Show colored Subways",
                    onCheckedChange = { checked ->
                        debuggingOptions = debuggingOptions.copy(showColoredSubways = checked)
                    }
                )
            }
            Box(Modifier.fillMaxSize()) {
                if (gameBoard != null)
                    BoardView(gameBoard, debuggingOptions)
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
    var gameBoard: GameBoard? by mutableStateOf(null)
    var player: Player? by mutableStateOf(null)

    Window(
        create = {
            ComposeWindow().apply {
                composeWindow = this
                title = "Cat and Mouse"
                isResizable = false
                setSize(800, 600)
            }
        },
        onKeyEvent = {
            runBlocking {
                val keyEvent = it
                val direction = if (keyEvent.type != KeyEventType.KeyDown) null else keyEvent.key.toDirection()
                if (direction != null) {
                    val command = PrivateMessage.MoveCommand(player = player, direction = direction)
                    try {
                        gameClient?.sendCommand(command)
                    } catch (e: Exception) {
                        println("Error sending command: $e")
                    }
                }
            }

            true
        },
        dispose = ComposeWindow::dispose,
    ) {
        App(gameBoard)
    }

    LaunchedEffect(true) {
        gameClient = GameClient(
            host = "127.0.0.1",
            port = 8080,
            onMapUpdate = {
                if (gameBoard == null) {
                    composeWindow.setContentSize(
                        it.columns * Constants.TILE_SIZE,
                        it.rows * Constants.TILE_SIZE + Constants.TOP_NAV_HEIGHT
                    )
                }
                gameBoard = it
            },
            onStateUpdate = {
                gameBoard = it.map
            },
            onSetupInfo = {
                player = it.player
            },
        )

        gameClient!!.start()
        gameClient!!.dispose()
    }
}
