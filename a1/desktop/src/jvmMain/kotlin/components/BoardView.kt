package components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.ac.tuwien.foop.common.domain.GameBoardDto
import components.primitives.CatView
import components.primitives.MouseView
import components.primitives.SubwayView

@Composable
fun BoardView(gameBoardDto: GameBoardDto, debuggingOptions: DebuggingOptions) {
    Box(
        modifier = Modifier.size(width = gameBoardDto.width.dp, height = gameBoardDto.height.dp)
    ) {
        //Tiles(gameBoard.width / Constants.TILE_SIZE, gameBoard.height / Constants.TILE_SIZE)
        gameBoardDto.subwayDtos.forEach {
            SubwayView(it, debuggingOptions = debuggingOptions)
        }
        gameBoardDto.mice.forEach {
            MouseView(it, alwaysVisible = debuggingOptions.showMouseTrace)
        }
        gameBoardDto.playerDtos.forEach {
            CatView(it)
        }
    }
}
