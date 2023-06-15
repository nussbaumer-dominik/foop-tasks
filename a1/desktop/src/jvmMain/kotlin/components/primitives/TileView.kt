package components.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TileView(x: Int, y: Int, tileSize: Int = 32) {
    Box(
        modifier = Modifier
            .size(tileSize.dp)
            .border(1.dp, Color.Black)
            .offset((x * tileSize).dp, (y * tileSize).dp)
    )
}