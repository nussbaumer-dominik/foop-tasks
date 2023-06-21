package at.ac.tuwien.foop.models

import at.ac.tuwien.foop.common.domain.Player

/**
 * Represents the session of a player
 */
data class PlayerConnection(
    /**
     * The player object associated with this session
     */
    val player: Player,
)