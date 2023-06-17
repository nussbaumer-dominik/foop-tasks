package components

import Constants
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import at.ac.tuwien.foop.common.domain.*
import components.primitives.CatView
import components.primitives.MouseView
import components.primitives.SubwayView
import components.primitives.TileView

@Composable
fun BoardView(gameBoard: GameBoard, onKeyEvent: (KeyEvent) -> Unit) {
    val requester = remember { FocusRequester() }
    val rows = gameBoard.rows
    val columns = gameBoard.columns
    Column {
        for (row in 0 until rows) {
            Row {
                for (column in 0 until columns) {
                    TileView(x = column, y = row, tileSize = Constants.TILE_SIZE)
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        var selectedImageOffset by remember {
            mutableStateOf(
                Position(
                    rows / 2,
                    columns / 2
                )
            )
        }
        CatView(Player(0, "Red", selectedImageOffset))
        MouseView(Mouse("1", Position(Constants.TILE_SIZE / 2, Constants.TILE_SIZE / 2), false, emptyList()))
        Image(
            bitmap = useResource("images/cat-head.png") { loadImageBitmap(it) },
            contentDescription = "Cat",
            modifier = Modifier
                .offset(
                    selectedImageOffset.x.dp * Constants.TILE_SIZE,
                    selectedImageOffset.y.dp * Constants.TILE_SIZE,
                )
                .onKeyEvent {
                    selectedImageOffset = handleKeyEvent(it, selectedImageOffset)
                    onKeyEvent(it)
                    true
                }
                .focusRequester(requester)
                .focusable(),
        )
        SubwayView(
            Subway(
                exits = mutableSetOf(
                    Exit(position = Position(2, 1)),
                    Exit(position = Position(3, 2)),
                ),
            )
        )
        SubwayView(
            Subway(
                exits = mutableSetOf(
                    Exit(position = Position(0, 0)),
                    Exit(position = Position(5, 2)),
                    Exit(position = Position(10, 10)),
                    Exit(position = Position(12, 10)),
                ),
            )
        )
    }
    LaunchedEffect(Unit) {
        requester.requestFocus()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun handleKeyEvent(
    keyEvent: KeyEvent,
    currentPosition: Position,
): Position {
    if (keyEvent.type != KeyEventType.KeyDown) return currentPosition
    val newPosition: Position = when (keyEvent.key) {
        Key.DirectionUp -> currentPosition.getNewPosition(Direction.UP)
        Key.DirectionDown -> currentPosition.getNewPosition(Direction.DOWN)
        Key.DirectionLeft -> currentPosition.getNewPosition(Direction.LEFT)
        Key.DirectionRight -> currentPosition.getNewPosition(Direction.RIGHT)
        else -> return currentPosition
    }

    println(newPosition)
    return newPosition
}
