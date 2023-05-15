package org.matthewo

import org.matthewo.framework.purepursuit.Pose
import kotlin.math.abs

class CarTrajectoryOptimizer {

    val table = TrajectoryTable(-90.0, 90.0, 1.0, -1.0, 1.0, 0.002, Pair(1000.0, 0.0))

    fun optimize(): TrajectoryTable {
        table[0.0, 0.0] = Pair(0.0, 0.0)

        val actions = (-27..27).toList()
        repeat(10) {
            table.updateAll { heading, distance, current ->
                val (currentCost, _) = current
                val state = Pose(0.0, distance, heading)
                val values = actions.map {
                    val action = it.toDouble()
                    val update = state.update(action, 1.0, 0.05)
                    val (cost, _) = table[update.heading, update.y]
                    val updatedCost = cost + abs(update.y) + abs(update.heading) / 90.0
                    if (updatedCost >= currentCost) {
                        current
                    } else {
                        Pair(updatedCost, action)
                    }
                }
                val opt = values.toList().minBy { it.first }
                opt
            }
        }

        return table
    }
}