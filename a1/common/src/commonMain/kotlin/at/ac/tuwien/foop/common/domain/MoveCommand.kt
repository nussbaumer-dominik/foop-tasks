package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A move command for a specific cat
 */
@Serializable
data class MoveCommand(
    @SerialName("id")
    val id: Long,
    @SerialName("direction")
    val direction: Direction,
)