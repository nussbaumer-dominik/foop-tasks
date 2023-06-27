package at.ac.tuwien.foop.common.models.dtos.rest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    @SerialName("username")
    val username: String,
)
