package components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp

@Composable
fun ScoreCard(
    catScore: Int,
    mouseScore: Int,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        catImage()
        Spacer(modifier = Modifier.size(16.dp))
        Row {
            Text(
                text = "$catScore : $mouseScore",
                style = MaterialTheme.typography.h3,
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        mouseImage()
    }
}

@Composable
fun mouseImage() {
    val density = LocalDensity.current.density
    val image = if (density <= 1) {
        useResource("images/mouse@64.png") { loadImageBitmap(it) }
    } else {
        useResource("images/mouse@128.png") { loadImageBitmap(it) }
    }

    Image(
        bitmap = image,
        contentDescription = "Mouse",
        modifier = Modifier
            .size(width = 64.dp, height = 64.dp)
    )
}

@Composable
fun catImage() {
    val density = LocalDensity.current.density
    val image = if (density <= 1) {
        useResource("images/cat-head@64.png") { loadImageBitmap(it) }
    } else {
        useResource("images/cat-head@128.png") { loadImageBitmap(it) }
    }

    Image(
        bitmap = image,
        contentDescription = "Cat",
        modifier = Modifier
            .size(width = 64.dp, height = 64.dp)
    )
}