package util

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import at.ac.tuwien.foop.common.domain.Direction

@OptIn(ExperimentalComposeUiApi::class)
fun Key.toDirection(): Direction? {
    return when (this) {
        Key.DirectionUp -> Direction.UP
        Key.DirectionDown -> Direction.DOWN
        Key.DirectionLeft -> Direction.LEFT
        Key.DirectionRight -> Direction.RIGHT
        else -> null
    }
}