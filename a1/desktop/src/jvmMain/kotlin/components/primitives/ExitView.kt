package components.primitives

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import at.ac.tuwien.foop.common.domain.Exit

@Composable
fun ExitView(exit: Exit) {
    val density = LocalDensity.current.density
    val radius = (Constants.TILE_SIZE / 2) * density
    val strokeWidth = 2 * density
    Canvas(
        modifier = Modifier
            .size(Constants.TILE_SIZE.dp)
            .offset {
                IntOffset(
                    exit.position.x * Constants.TILE_SIZE,
                    exit.position.y * Constants.TILE_SIZE
                )
            },
    ) {
        drawCircle(
            color = Color.Black,
            radius = (radius - strokeWidth / 2f),
            center = center,
            style = Stroke(width = strokeWidth)
        )
    }
}