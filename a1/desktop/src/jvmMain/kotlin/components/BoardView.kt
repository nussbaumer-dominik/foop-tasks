package components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import at.ac.tuwien.foop.common.domain.GameBoard
import components.primitives.*

@Composable
fun BoardView(gameBoard: GameBoard) {
    Tiles(gameBoard.rows, gameBoard.columns)
    Box(modifier = Modifier.fillMaxSize()) {
        gameBoard.cats.forEach {
            CatView(it)
        }
        gameBoard.mice.forEach {
            MouseView(it)
        }
        gameBoard.subways.forEach {
            SubwayView(it)
        }
    }
}
