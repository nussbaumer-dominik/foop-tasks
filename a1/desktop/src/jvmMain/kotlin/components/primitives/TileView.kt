package components.primitives

import Constants
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TileView(x: Int, y: Int) {
    Box(
        modifier = Modifier
            .size(Constants.TILE_SIZE.dp)
            .border(1.dp, Color.Black)
            .offset((x * Constants.TILE_SIZE).dp, (y * Constants.TILE_SIZE).dp)
    )
}