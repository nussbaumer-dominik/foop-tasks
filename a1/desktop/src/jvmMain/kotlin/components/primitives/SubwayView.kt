package components.primitives

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import at.ac.tuwien.foop.common.models.domain.socket.Subway
import at.ac.tuwien.foop.common.models.util.generateHSL
import components.DebuggingOptions
import helper.toColor

@Composable
fun SubwayView(
    subway: Subway,
    debuggingOptions: DebuggingOptions
) {
    val color = if (debuggingOptions.showColoredSubways) {
        subway.id.generateHSL().toColor()
    } else {
        Color.Black
    }

    for (exit in subway.exits) {
        ExitView(exit, color)
    }
}
