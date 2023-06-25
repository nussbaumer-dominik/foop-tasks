package components.primitives

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import at.ac.tuwien.foop.common.models.domain.socket.Subway
import components.DebuggingOptions
import helper.generateHSL

@Composable
fun SubwayView(
    subway: Subway,
    debuggingOptions: DebuggingOptions
) {
    val color = if (debuggingOptions.showColoredSubways) {
        subway.id.generateHSL()
    } else {
        Color.Black
    }

    for (exit in subway.exits) {
        ExitView(exit, color)
    }
}
