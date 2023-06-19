package at.ac.tuwien.foop.common.domain

import kotlinx.serialization.Serializable

@Serializable
data class Way(
    val startingPosition: Position,
    val targetPosition: Position,
    val distance: Int,
    val subwayId: String?,
    val usingExit: Boolean = false
) {
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as Way
//
//        if (!(
//                    (startingPosition == other.startingPosition && targetPosition == other.targetPosition) ||
//                            (startingPosition == other.targetPosition && targetPosition == other.startingPosition)
//                )
//            ) return false
//        if (distance != other.distance) return false
//        return subway == other.subway
//    }

//    override fun hashCode(): Int {
//        var result = startingPosition.hashCode() + targetPosition.hashCode()
//        result = 31 * result + distance
//        result = 31 * result + (subwayId?.hashCode() ?: 0)
//        return result
//    }

    override fun toString(): String {
        return "Way(startingPosition=$startingPosition, targetPosition=$targetPosition, distance=$distance, viaSubway=${subwayId!=null})"
    }


}
