package components.primitives

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import at.ac.tuwien.foop.common.domain.Mouse

@Composable
fun MouseView(mouse: Mouse) {
    if (mouse.subway != null) return
    val density = LocalDensity.current.density

    Image(
        bitmap = useResource("images/mouse.png") { loadImageBitmap(it) },
        contentDescription = "Mouse",
        modifier = Modifier.offset(
            mouse.position.x.dp * 32 * density,
            mouse.position.y.dp * 32 * density
        )
    )
}
