package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

/**
 * A subway which is positioned on the map and has its exits
 * */
@Serializable
data class Subway(
    /**
     * The unique id of a subway which is assigned when it is spawned
     */
    @SerialName("id")
    val id: String = UUID.randomUUID().toString(),
    /**
     * The exits that exist on this subway
     * */
    @SerialName("exits")
    val exits: MutableSet<Exit> = mutableSetOf(),
) {
    fun addExit(exit: Exit): Boolean {
        if (containsExit(exit)) {
            return false
        }

        exits.add(exit)
        return true
    }

    private fun containsExit(exit: Exit): Boolean {
        return exits.stream().anyMatch { e -> e.position == exit.position }
    }
}
