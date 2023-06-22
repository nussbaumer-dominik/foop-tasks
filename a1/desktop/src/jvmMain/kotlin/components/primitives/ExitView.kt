package components.primitives

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import at.ac.tuwien.foop.common.domain.Exit

@Composable
fun ExitView(exit: Exit, color: Color = Color.Black) {
    val density = LocalDensity.current.density
    val radius = (exit.size.width / 2) * density
    val strokeWidth = 2 * density

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Canvas(
        modifier = Modifier
            .size(width = exit.size.width.dp, height = exit.size.height.dp)
            .offset(x = exit.position.x.dp, y = exit.position.y.dp)
            .border(1.dp, Color.Black)
            .hoverable(interactionSource = interactionSource)
    ) {
        drawCircle(
            color = color,
            radius = (radius - strokeWidth / 2f),
            center = center,
            style = Stroke(width = strokeWidth)
        )
        drawCircle(
            color = Color.Red,
            radius = 2f,
            center = center,
            style = Fill
        )

    }
    if (isHovered)
        Text(
            text = "Position: (${exit.position.x}, ${exit.position.y})",
            modifier = Modifier.offset(x = exit.position.x.dp, y = exit.position.y.dp)
        )
}

// .offset(x = (exit.position.x - exit.size.width).dp, y = (exit.position.y - exit.size.height).dp),