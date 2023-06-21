package components.primitives

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable

@Composable
fun Tiles(rows: Int, columns: Int, visible: Boolean = true) {
    if (!visible) return

    Column {
        for (row in 0 until rows) {
            Row {
                for (column in 0 until columns) {
                    TileView(x = column, y = row)
                }
            }
        }
    }
}