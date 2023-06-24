package at.ac.tuwien.foop.domain.mouseStrategy

import at.ac.tuwien.foop.common.domain.Direction
import at.ac.tuwien.foop.domain.GameBoard
import at.ac.tuwien.foop.domain.Mouse
import at.ac.tuwien.foop.domain.Position

class MouseRandomStrategy : MouseStrategy() {
    override fun newPosition(mouse: Mouse, gameBoard: GameBoard): Position {
        val directions = Direction.values().toMutableSet()
        while (directions.isNotEmpty()) {
            val direction = directions.random()
            directions.remove(direction)
            val tempMouse = mouse.toEntity()
            tempMouse.position = tempMouse.position.getNewPosition(direction, tempMouse.moveSize)

            for (subway in gameBoard.subways) {
                for (exit in subway.exits) {
                    if (tempMouse.intersects(exit)) {
                        mouse.subway = subway
                        return tempMouse.position
                    }
                }
            }
        }

        return mouse.position
    }
}