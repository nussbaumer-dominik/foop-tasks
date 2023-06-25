package screens.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.ac.tuwien.foop.common.client.A1RestClient
import at.ac.tuwien.foop.common.models.domain.rest.RegisterRequest
import at.ac.tuwien.foop.common.models.exceptions.DuplicatedUsernameException
import game.A1Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import screens.A1Screen
import screens.navigator.A1Navigator
import screens.navigator.NavigationDestination

class StartScreen(
    private val game: A1Game,
    private val restClient: A1RestClient,
    private val navigator: A1Navigator,
) : A1Screen<StartScreen.ViewState>(ViewState()) {
    @Composable
    override fun render() {
        val screenScope = rememberCoroutineScope()
        val viewState by observeState().collectAsState()

        var username by remember { mutableStateOf("") }

        MaterialTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.4f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "Welcome to A1",
                        color = Color.Black,
                        style = MaterialTheme.typography.h4,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(0.7f),
                        text = "Choose a unique username to join the lobby",
                        color = Color.Gray,
                        style = MaterialTheme.typography.subtitle1,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(
                        modifier = Modifier
                            .height(20.dp),
                    )
                    BasicTextField(
                        value = username,
                        onValueChange = { value ->
                            username = value
                            updateState { it.copy(username = value) }
                        },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center, fontSize = 24.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFFFFFFF))
                            .padding(16.dp),
                    )
                    Spacer(
                        modifier = Modifier
                            .height(10.dp),
                    )
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Black,
                            contentColor = Color.White,
                        ),
                        onClick = { screenScope.launch { joinLobby() } },
                    ) {
                        Text(
                            text = "Join lobby",
                            style = MaterialTheme.typography.button,
                        )
                    }
                    if (viewState.error != null)
                        Text(
                            text = viewState.error ?: "",
                            color = Color.Red,
                            style = MaterialTheme.typography.caption,
                        )
                }
            }
        }
    }

    private suspend fun joinLobby() = withContext(Dispatchers.Default) {
        val result = restClient.join(RegisterRequest(username = getState().username))
        if (result.isFailure) {
            if (result.exceptionOrNull() is DuplicatedUsernameException) {
                updateState { it.copy(error = "Username already exists") }
            } else {
                updateState { it.copy(error = result.exceptionOrNull()?.message) }
            }
            return@withContext
        }

        val response = result.getOrNull()
        if (response == null) {
            updateState { it.copy(error = "An error occurred") }
            return@withContext
        }

        game.updateCurrentPlayerId(response.id)
        navigator.navigate(NavigationDestination.LOBBY)
    }

    data class ViewState(
        val username: String = "",
        val error: String? = null,
    )
}
