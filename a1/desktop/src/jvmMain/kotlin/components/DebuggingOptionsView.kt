package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import components.primitives.OptionTile

data class DebuggingOptions(
    val showMouseTrace: Boolean = false,
    val showEmptyTiles: Boolean = true,
    val showColoredSubways: Boolean = false,
)

@Composable
fun DebuggingOptionsView(
    debuggingOptions: DebuggingOptions,
    onChange: (DebuggingOptions) -> Unit
) {
    Row(
        modifier = Modifier
            .height(height = 50.dp)
            .fillMaxWidth()
            .background(color = Color.Cyan),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        OptionTile(
            checked = debuggingOptions.showMouseTrace,
            text = "Show mouse trace",
            onCheckedChange = { checked ->
                onChange(debuggingOptions.copy(showMouseTrace = checked))
            }
        )
        OptionTile(
            checked = debuggingOptions.showColoredSubways,
            text = "Show colored Subways",
            onCheckedChange = { checked ->
                onChange(debuggingOptions.copy(showColoredSubways = checked))
            }
        )
    }
}
