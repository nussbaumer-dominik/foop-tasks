package at.ac.tuwien.foop.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    @SerialName("text")
    val text: String,
)
