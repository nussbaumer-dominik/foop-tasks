package components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.ac.tuwien.foop.common.domain.GameBoard
import components.primitives.CatView
import components.primitives.MouseView
import components.primitives.SubwayView

@Composable
fun BoardView(gameBoard: GameBoard, debuggingOptions: DebuggingOptions) {
    Box(
        modifier = Modifier.size(width = gameBoard.width.dp, height = gameBoard.height.dp)
    ) {
        //Tiles(gameBoard.width / Constants.TILE_SIZE, gameBoard.height / Constants.TILE_SIZE)
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
