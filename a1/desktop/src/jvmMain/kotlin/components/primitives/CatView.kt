package components.primitives

import Constants
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import at.ac.tuwien.foop.common.domain.Player

@Composable
fun CatView(cat: Player) {
    val density = LocalDensity.current.density
    val image = if (density <= 1) {
        useResource("images/cat-head@32.png") { loadImageBitmap(it) }
    } else {
        useResource("images/cat-head@64.png") { loadImageBitmap(it) }
    }
    Image(
        bitmap = image,
        contentDescription = "Cat",
        modifier = Modifier
            .offset(
                (cat.position.x * Constants.TILE_SIZE).dp,
                (cat.position.y * Constants.TILE_SIZE).dp
            ).size(Constants.TILE_SIZE.dp)
    )
    /*
    Canvas(modifier = Modifier.size(32.dp).align(Alignment.Center)) {
            drawCircle(
                color = Color.Red,
                radius = 16f,
                center = Offset(16f, 16f),
                style = Fill
            )
        }
     */
}