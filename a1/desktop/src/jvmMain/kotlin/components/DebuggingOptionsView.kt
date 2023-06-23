package components

import androidx.compose.runtime.Composable
import components.primitives.OptionTile

@Composable
fun DebuggingOptionsView(
    debuggingOptions: DebuggingOptions,
    onChange: (DebuggingOptions) -> Unit
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