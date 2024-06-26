package components.primitives

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import at.ac.tuwien.foop.common.models.domain.socket.Player
import helper.toColor

@Composable
fun CatView(
    cat: Player,
) {
    val density = LocalDensity.current.density
    val image = if (density <= 1) {
        useResource("images/cat-head@32.png") { loadImageBitmap(it) }
    } else {
        useResource("images/cat-head@64.png") { loadImageBitmap(it) }
    }

    val playerColor = cat.color.toColor()
    val colorFilter = ColorFilter.tint(playerColor, BlendMode.SrcIn)

    Image(
        bitmap = image,
        contentDescription = "Cat",
        colorFilter = colorFilter,
        modifier = Modifier
            .offset(x = cat.position.x.dp, y = cat.position.y.dp)
            .size(width = 32.dp, height = 32.dp)
    )
}