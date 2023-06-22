package components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import at.ac.tuwien.foop.common.domain.GameBoard
import components.primitives.CatView
import components.primitives.MouseView
import components.primitives.SubwayView
import components.primitives.Tiles

@Composable
fun BoardView(gameBoard: GameBoard, debuggingOptions: DebuggingOptions) {
    Tiles(gameBoard.rows, gameBoard.columns, debuggingOptions.showEmptyTiles)
    Box(modifier = Modifier.fillMaxSize()) {
        gameBoard.cats.forEach {
            CatView(it)
        }
        gameBoard.mice.forEach {
            MouseView(it, alwaysVisible = debuggingOptions.showMouseTrace)
        }
        gameBoard.subways.forEach {
            SubwayView(it, debuggingOptions = debuggingOptions)
        }
    }
}
