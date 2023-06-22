package components.primitives

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import at.ac.tuwien.foop.common.domain.Exit

@Composable
fun ExitView(exit: Exit, color: Color = Color.Black) {
    val density = LocalDensity.current.density
    val radius = (exit.size.width / 2) * density
    val strokeWidth = 2 * density

    Canvas(
        modifier = Modifier
            .size(width = exit.size.width.dp, height = exit.size.height.dp)
            .offset(x = exit.position.x.dp, y = exit.position.y.dp)
    ) {
        drawCircle(
            color = color,
            radius = (radius - strokeWidth / 2f),
            center = center,
            style = Stroke(width = strokeWidth)
        )
    }
}