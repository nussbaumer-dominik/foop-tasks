package at.ac.tuwien.foop.common.models.mapper

import at.ac.tuwien.foop.common.models.domain.socket.GameStatus
import at.ac.tuwien.foop.common.models.dtos.socket.GameStatusDto

fun GameStatus.mapToDto(): GameStatusDto = when (this) {
    GameStatus.WAITING -> GameStatusDto.WAITING
    GameStatus.RUNNING -> GameStatusDto.RUNNING
    GameStatus.MICE_WON -> GameStatusDto.MICE_WON
    GameStatus.CATS_WON -> GameStatusDto.CATS_WON
}

fun GameStatusDto.map(): GameStatus = when (this) {
    GameStatusDto.WAITING -> GameStatus.WAITING
    GameStatusDto.RUNNING -> GameStatus.RUNNING
    GameStatusDto.MICE_WON -> GameStatus.MICE_WON
    GameStatusDto.CATS_WON -> GameStatus.CATS_WON
}
