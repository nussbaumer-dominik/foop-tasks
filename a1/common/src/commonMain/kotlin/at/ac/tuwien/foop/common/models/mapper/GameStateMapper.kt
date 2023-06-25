package at.ac.tuwien.foop.common.models.mapper

import at.ac.tuwien.foop.common.models.domain.socket.GameState
import at.ac.tuwien.foop.common.models.dtos.socket.GameStatusDto

fun GameState.mapToDto(): GameStatusDto = when (this) {
    GameState.WAITING -> GameStatusDto.WAITING
    GameState.RUNNING -> GameStatusDto.RUNNING
    GameState.MICE_WON -> GameStatusDto.MICE_WON
    GameState.CATS_WON -> GameStatusDto.CATS_WON
}

fun GameStatusDto.map(): GameState = when (this) {
    GameStatusDto.WAITING -> GameState.WAITING
    GameStatusDto.RUNNING -> GameState.RUNNING
    GameStatusDto.MICE_WON -> GameState.MICE_WON
    GameStatusDto.CATS_WON -> GameState.CATS_WON
}
