package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.Serializable

/**
 * The available directions for a key press
 */
@Serializable
enum class Direction {
    UP, DOWN, LEFT, RIGHT, ACCESS_SUBWAY, STAY
}
