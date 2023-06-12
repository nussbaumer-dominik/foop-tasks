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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource

@Preview
@Composable
private fun BoardPreview() {
    Board()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Board() {
    val requester = remember { FocusRequester() }
    Box {
        val selectedImageOffset = remember { mutableStateOf(Offset.Zero) }
        Canvas(
            modifier = Modifier.fillMaxSize()
                .onKeyEvent {
                    handleKeyEvent(it, selectedImageOffset)
                    true
                }
                .focusRequester(requester)
                .focusable()
            /*.onPointerEvent(
                eventType = androidx.compose.ui.input.pointer.PointerEventType.Move,
                onEvent = {
                    selectedImageOffset.value = it.changes.first().position
                }
            )*/
        ) {
            val height = size.height
            for (i in 32..height.toInt() - 16 step 100) {
                drawMouse(Offset(i.toFloat(), 32f), Size(32f, 32f))
            }
            drawCat(selectedImageOffset.value)
        }
    }
    LaunchedEffect(Unit) {
        requester.requestFocus()
    }
}

fun DrawScope.drawMouse(position: Offset, size: Size = Size(64f, 64f)) {
    val image = useResource("images/mouse.png", ::loadImageBitmap)
    drawImage(image, position.minus(Offset(size.width / 2, size.height / 2)))
}

fun DrawScope.drawCat(position: Offset, size: Size = Size(64f, 64f)) {
    val image = useResource("images/cat-head.png", ::loadImageBitmap)
    drawImage(image, position.minus(Offset(size.width / 2, size.height / 2)))
}

@OptIn(ExperimentalComposeUiApi::class)
private fun handleKeyEvent(
    keyEvent: KeyEvent,
    selectedImageOffset: MutableState<Offset>,
    speed: Float = 2f
) {
    when (keyEvent.key) {
        Key.DirectionUp -> selectedImageOffset.value -= Offset(0f, speed)
        Key.DirectionDown -> selectedImageOffset.value += Offset(0f, speed)
        Key.DirectionLeft -> selectedImageOffset.value -= Offset(speed, 0f)
        Key.DirectionRight -> selectedImageOffset.value += Offset(speed, 0f)
        else -> return
    }
}