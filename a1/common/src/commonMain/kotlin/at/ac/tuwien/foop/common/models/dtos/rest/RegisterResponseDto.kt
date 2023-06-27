package at.ac.tuwien.foop.common.models.dtos.rest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("username")
    val username: String,
)
