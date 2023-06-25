package components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import at.ac.tuwien.foop.common.models.domain.socket.GameBoard
import components.primitives.CatView
import components.primitives.MouseView
import components.primitives.SubwayView

@Composable
fun BoardView(
    gameBoard: GameBoard,
    debuggingOptions: DebuggingOptions,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        gameBoard.subways.forEach {
            SubwayView(it, debuggingOptions = debuggingOptions)
        }
        gameBoard.mice.forEach {
            MouseView(it, alwaysVisible = debuggingOptions.showMouseTrace)
        }
        gameBoard.players.forEach {
            CatView(it)
        }
    }
}
