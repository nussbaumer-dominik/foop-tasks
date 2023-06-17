package components.primitives

import androidx.compose.runtime.Composable
import at.ac.tuwien.foop.common.domain.Subway

@Composable
fun SubwayView(subway: Subway) {
    for (exit in subway.exits) {
        ExitView(exit)
    }
}