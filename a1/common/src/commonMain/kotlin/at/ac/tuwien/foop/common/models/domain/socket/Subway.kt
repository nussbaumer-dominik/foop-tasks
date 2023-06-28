package at.ac.tuwien.foop.common.models.domain.socket

data class Subway(
    val id: String,
    val exits: Set<Exit>,
)
