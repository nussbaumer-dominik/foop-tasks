package components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import at.ac.tuwien.foop.common.domain.Exit
import at.ac.tuwien.foop.common.domain.Position
import at.ac.tuwien.foop.common.domain.Subway

@Preview
@Composable
private fun BoardPreview() {
    BoardView(tileSize = 32, rows = 30, columns = 30)
}

@Composable
fun BoardView(tileSize: Int = 32, rows: Int, columns: Int) {
    val requester = remember { FocusRequester() }
    Box {
        val selectedImageOffset = mutableStateOf(Offset((rows * tileSize) / 2f, (columns * tileSize) / 2f))
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onKeyEvent {
                    handleKeyEvent(it, selectedImageOffset)
                    true
                }
                .focusRequester(requester)
                .focusable()
        ) {
            //selectedImageOffset.value = center
            drawBoard(rows, columns, tileSize)
            drawMouse(Offset(tileSize.toFloat(), 0f))
            drawCat(selectedImageOffset.value)
            drawSubway(
                Subway(
                    exits = setOf(
                        Exit(position = Position(2, 1)),
                        Exit(position = Position(3, 2)),
                    ),
                )
            )
            drawSubway(
                Subway(
                    exits = setOf(
                        Exit(position = Position(0, 0)),
                        Exit(position = Position(5, 2)),
                        Exit(position = Position(10, 10)),
                        Exit(position = Position(12, 10)),
                    ),
                )
            )
        }
    }
    LaunchedEffect(Unit) {
        requester.requestFocus()
    }
}

fun DrawScope.drawBoard(rows: Int, columns: Int, tileSize: Int = 32) {
    for (i in 0..rows) {
        for (j in 0..columns) {
            val color = if ((i + j) % 2 == 0) Color.LightGray else Color.White
            drawTile(i * tileSize, j * tileSize, tileSize, color)
        }
    }
}

fun DrawScope.drawTile(x: Int, y: Int, size: Int, color: Color) {
    drawRect(
        topLeft = Offset(x.dp.toPx(), y.dp.toPx()),
        size = Size(size.dp.toPx(), size.dp.toPx()),
        color = color,
    )
}

fun DrawScope.drawMouse(position: Offset) {
    val image = useResource("images/mouse.png", ::loadImageBitmap)
    drawImage(image, position)
}

fun DrawScope.drawCat(position: Offset) {
    val image = useResource("images/cat-head.png", ::loadImageBitmap)
    drawImage(image = image, topLeft = position.minus(Offset(16.dp.toPx(), 16.dp.toPx())))
}

fun DrawScope.drawSubway(subway: Subway) {
    for (exit in subway.exits) {
        drawExit(exit)
    }
}

fun DrawScope.drawExit(exit: Exit) {
    val radius = (32 / 2).dp.toPx()
    val strokeWidth = 2.dp.toPx()
    drawCircle(
        color = Color.Black,
        radius = radius - strokeWidth / 2,
        center = Offset((exit.position.x * 32) + radius, (exit.position.y * 32) + radius),
        style = Stroke(width = strokeWidth)
    )
}

@OptIn(ExperimentalComposeUiApi::class)
private fun handleKeyEvent(
    keyEvent: KeyEvent,
    selectedImageOffset: MutableState<Offset>,
    speed: Float = 32.dp.value
) {
    when (keyEvent.key) {
        Key.DirectionUp -> selectedImageOffset.value -= Offset(0f, speed)
        Key.DirectionDown -> selectedImageOffset.value += Offset(0f, speed)
        Key.DirectionLeft -> selectedImageOffset.value -= Offset(speed, 0f)
        Key.DirectionRight -> selectedImageOffset.value += Offset(speed, 0f)
        else -> return
    }
}