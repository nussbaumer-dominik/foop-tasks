package components.primitives

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import at.ac.tuwien.foop.common.models.domain.socket.Mouse

@Composable
fun MouseView(
    mouse: Mouse,
    alwaysVisible: Boolean = false
) {
    if (mouse.subway != null && !alwaysVisible) return
    val density = LocalDensity.current.density
    val image = if (density <= 1) {
        useResource("images/mouse@32.png") { loadImageBitmap(it) }
    } else {
        useResource("images/mouse@64.png") { loadImageBitmap(it) }
    }

    val colorFilter = if (mouse.subway != null && alwaysVisible)
        ColorFilter.tint(Color.Gray, BlendMode.SrcIn)
    else null

    Image(
        bitmap = image,
        contentDescription = "Mouse",
        colorFilter = colorFilter,
        modifier = Modifier
            .offset(x = mouse.position.x.dp, y = mouse.position.y.dp)
            .size(width = mouse.size.width.dp, height = mouse.size.height.dp)
    )
}
