package components.primitives

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import at.ac.tuwien.foop.common.domain.Subway
import components.DebuggingOptions
import util.ColorGenerator

@Composable
fun SubwayView(
    subway: Subway,
    debuggingOptions: DebuggingOptions
) {
    val color = if (debuggingOptions.showColoredSubways) {
        ColorGenerator.generateHSL(subway.id)
    } else {
        Color.Black
    }

    for (exit in subway.exits) {
        ExitView(exit, color)
    }
}