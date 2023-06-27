package at.ac.tuwien.foop.common.models.dtos.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A subway which is positioned on the map and has its exits
 * */
@Serializable
data class SubwayDto(
    /**
     * The unique id of a subway which is assigned when it is spawned
     */
    @SerialName("id")
    val id: String,
    /**
     * The exits that exist on this subway
     * */
    @SerialName("exits")
    val exits: Set<ExitDto>,
)
