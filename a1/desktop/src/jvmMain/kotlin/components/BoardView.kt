package components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.ac.tuwien.foop.common.models.domain.socket.GameBoard
import at.ac.tuwien.foop.common.models.domain.socket.Player
import components.primitives.CatView
import components.primitives.MouseView
import components.primitives.SubwayView
import helper.toColor

@Composable
fun BoardView(
    gameBoard: GameBoard,
    currentPlayer: Player?,
    debuggingOptions: DebuggingOptions,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
                .height(50.dp)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val playerColor: Color = currentPlayer?.color?.toColor() ?: Color.Black

            Card(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .wrapContentWidth(Alignment.Start)
                    .size(height = 40.dp, width = Dp.Unspecified),
                backgroundColor = playerColor,
                elevation = 0.dp,
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = currentPlayer?.username ?: "Player",
                        color = if (playerColor.luminance() < 0.6) Color.White else Color.Black,
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp),
                text = "${
                    gameBoard.mice.filter {
                        !it.isDead && it.subway != gameBoard.winningSubway
                    }.size
                } mice left",
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black,
                textAlign = TextAlign.Center,
            )
        }
        Divider(
            color = Color.Black,
            thickness = 1.dp,
        )
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            gameBoard.subways.forEach {
                SubwayView(it, debuggingOptions = debuggingOptions)
            }
            gameBoard.mice.filter { !it.isDead }.forEach {
                MouseView(it, alwaysVisible = debuggingOptions.showMouseTrace)
            }
            gameBoard.players.forEach {
                CatView(it)
            }
        }
    }
}
