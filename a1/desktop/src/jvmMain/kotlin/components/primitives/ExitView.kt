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
import at.ac.tuwien.foop.common.domain.ExitDto

@Composable
fun ExitView(exitDto: ExitDto, color: Color = Color.Black) {
    val density = LocalDensity.current.density
    val radius = (exitDto.sizeDto.width / 2) * density
    val strokeWidth = 2 * density

    Canvas(
        modifier = Modifier
            .size(width = exitDto.sizeDto.width.dp, height = exitDto.sizeDto.height.dp)
            .offset(x = exitDto.positionDto.x.dp, y = exitDto.positionDto.y.dp)
    ) {
        drawCircle(
            color = color,
            radius = (radius - strokeWidth / 2f),
            center = center,
            style = Stroke(width = strokeWidth)
        )
    }
}