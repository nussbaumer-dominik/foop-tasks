package components.primitives

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import at.ac.tuwien.foop.common.domain.Exit

@Composable
fun ExitView(exit: Exit) {
    val radius = (32 / 2)
    val strokeWidth = 2
    Canvas(
        modifier = Modifier
            .size(32.dp)
            .offset { IntOffset(exit.position.x * 32, exit.position.y * 32) },
    ) {
        drawCircle(
            color = Color.Black,
            radius = (radius - strokeWidth / 2f),
            center = center,
            style = Stroke(width = strokeWidth.toFloat())
        )
    }
}