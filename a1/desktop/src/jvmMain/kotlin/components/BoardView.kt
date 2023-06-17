package components

import Constants
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import at.ac.tuwien.foop.common.domain.GameBoard
import at.ac.tuwien.foop.common.domain.Player
import at.ac.tuwien.foop.common.domain.Position
import components.primitives.CatView
import components.primitives.MouseView
import components.primitives.SubwayView
import components.primitives.TileView

@Composable
fun BoardView(gameBoard: GameBoard) {
    val rows = gameBoard.rows
    val columns = gameBoard.columns

    Column {
        for (row in 0 until rows) {
            Row {
                for (column in 0 until columns) {
                    TileView(x = column, y = row, tileSize = Constants.TILE_SIZE)
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        /*gameBoard.cats.forEach{
            CatView(it)
        }*/
        CatView(Player(0, "Red", Position(0, 0)))
        gameBoard.mice.forEach {
            MouseView(it)
        }
        gameBoard.subways.forEach {
            SubwayView(it)
        }
    }
}
