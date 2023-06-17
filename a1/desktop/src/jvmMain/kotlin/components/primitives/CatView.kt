package components.primitives

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import at.ac.tuwien.foop.common.domain.Player

@Composable
fun CatView(cat: Player) {
    Image(
        bitmap = useResource("images/cat-head.png") { loadImageBitmap(it) },
        contentDescription = "Cat",
        modifier = Modifier.offset(cat.position.x.dp * 32, cat.position.y.dp * 32)
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