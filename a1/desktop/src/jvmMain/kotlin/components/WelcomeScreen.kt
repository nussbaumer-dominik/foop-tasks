package components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Preview
@Composable
private fun WelcomeScreenPreview() {
    MaterialTheme {
        WelcomeScreen(emptyList())
    }
}

@Composable
fun WelcomeScreen(messages: List<String>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        items(messages) {
            Text(
                text = it,
                style = MaterialTheme.typography.body1,
            )
        }
    }
}