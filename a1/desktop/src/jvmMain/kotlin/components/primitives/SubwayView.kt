package components.primitives

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import at.ac.tuwien.foop.common.domain.SubwayDto
import components.DebuggingOptions
import util.ColorGenerator

@Composable
fun SubwayView(
    subway: SubwayDto,
    debuggingOptions: DebuggingOptions
) {
    val color = if (debuggingOptions.showColoredSubways) {
        ColorGenerator.generateHSL(subway.id)
    } else {
        Color.Black
    }

    for (exit in subway.exitDtos) {
        ExitView(exit, color)
    }
}