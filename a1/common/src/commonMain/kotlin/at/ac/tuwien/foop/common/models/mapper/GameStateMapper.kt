package at.ac.tuwien.foop.common.models.mapper

import at.ac.tuwien.foop.common.models.domain.socket.GameStatus
import at.ac.tuwien.foop.common.models.dtos.socket.GameStatusDto

fun GameStatus.mapToDto(): GameStatusDto = when (this) {
    GameStatus.WAITING -> GameStatusDto.WAITING
    GameStatus.RUNNING -> GameStatusDto.RUNNING
    GameStatus.GAME_OVER -> GameStatusDto.GAME_OVER
}

fun GameStatusDto.map(): GameStatus = when (this) {
    GameStatusDto.WAITING -> GameStatus.WAITING
    GameStatusDto.RUNNING -> GameStatus.RUNNING
    GameStatusDto.GAME_OVER -> GameStatus.GAME_OVER
}
