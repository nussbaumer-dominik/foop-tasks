package at.ac.tuwien.foop.common.models.mapper

import at.ac.tuwien.foop.common.models.domain.rest.RegisterRequest
import at.ac.tuwien.foop.common.models.domain.rest.RegisterResponse
import at.ac.tuwien.foop.common.models.dtos.rest.RegisterRequestDto
import at.ac.tuwien.foop.common.models.dtos.rest.RegisterResponseDto

fun RegisterRequest.mapToDto(): RegisterRequestDto = RegisterRequestDto(
    username = username,
)

fun RegisterRequestDto.map(): RegisterRequest = RegisterRequest(
    username = username,
)

fun RegisterResponseDto.map(): RegisterResponse = RegisterResponse(
    id = id,
    username = username,
)

fun RegisterResponse.mapToDto(): RegisterResponseDto = RegisterResponseDto(
    id = id,
    username = username,
)
