package at.ac.tuwien.foop.common.models.mapper

import at.ac.tuwien.foop.common.models.domain.socket.GameBoard
import at.ac.tuwien.foop.common.models.domain.socket.Mouse
import at.ac.tuwien.foop.common.models.domain.socket.Player
import at.ac.tuwien.foop.common.models.domain.socket.Subway
import at.ac.tuwien.foop.common.models.dtos.socket.GameBoardDto
import at.ac.tuwien.foop.common.models.dtos.socket.MouseDto
import at.ac.tuwien.foop.common.models.dtos.socket.PlayerDto
import at.ac.tuwien.foop.common.models.dtos.socket.SubwayDto

fun GameBoard.mapToDto(): GameBoardDto = GameBoardDto(
    subways = subways.map(Subway::mapToDto).toSet(),
    mice = mice.map(Mouse::mapToDto).toSet(),
    players = players.map(Player::mapToDto).toSet(),
    width = width,
    height = height,
    winningSubway = winningSubway.mapToDto(),
)

fun GameBoardDto.map(): GameBoard = GameBoard(
    subways = subways.map(SubwayDto::map).toSet(),
    mice = mice.map(MouseDto::map).toSet(),
    players = players.map(PlayerDto::map).toSet(),
    width = width,
    height = height,
    winningSubway = winningSubway.map(),
)
 