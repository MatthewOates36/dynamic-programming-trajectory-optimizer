package org.matthewo

import org.matthewo.framework.purepursuit.Pose
import java.io.File
import java.io.FileWriter
import java.nio.file.Path
import kotlin.math.ceil
import kotlin.math.roundToInt

class TrajectoryOptimizer {

    val table = TrajectoryTable(-180.0, 180.0, 1.0, -400.0, 400.0, 2.0, Pair(Double.POSITIVE_INFINITY, 0.0))

    fun optimize(): TrajectoryTable {
        val target = PendulumState(90.0, 0.0)

        table[target.position, target.velocity] = Pair(0.0, 0.0)

        repeat(1) {
            for (i in -10..10) {
                table.updateAll { position, velocity, current ->
                    val action = i / 2.0
                    val (currentCost, _) = current
                    val state = PendulumState(position, velocity)
                    val update = state.update(action)
                    val (cost, _) = table[Pose.normalizeAngle(update.position), update.velocity]
                    val updatedCost = cost + 1.0
                    if (updatedCost > currentCost) {
                        current
                    } else {
                        Pair(updatedCost, action)
                    }
                }
            }
            for (i in 10 downTo -10) {
                table.updateAll { position, velocity, current ->
                    val action = i / 2.0
                    val (currentCost, _) = current
                    val state = PendulumState(position, velocity)
                    val update = state.update(action)
                    val (cost, _) = table[Pose.normalizeAngle(update.position), update.velocity]
                    val updatedCost = cost + 1.0
                    if (updatedCost > currentCost) {
                        current
                    } else {
                        Pair(updatedCost, action)
                    }
                }
            }
        }

//        table.iterate { position, velocity, (cost, action) ->
//            if(cost < Double.POSITIVE_INFINITY && position < 0) {
//                println("$position, $velocity, $cost, $action")
//            }
//        }

        return table
    }
}

class TrajectoryTable(val start1: Double, val end1: Double, val resolution1: Double, val start2: Double, val end2: Double, val resolution2: Double, val init: Pair<Double, Double>) {

    val step1 = 1 / resolution1
    val step2 = 1 / resolution2

    val size1 = ceil((end1 - start1) * step1).roundToInt()
    val size2 = ceil((end2 - start2) * step2).roundToInt()

    val table = Array(size1) {
        Array(size2) {
            init
        }
    }

    operator fun get(v1: Double, v2: Double): Pair<Double, Double> {
        val idx1 = ((v1 - start1) * step1).roundToInt().coerceIn(0 until size1)
        val idx2 = ((v2 - start2) * step2).roundToInt().coerceIn(0 until size2)

        return table[idx1][idx2]
    }

    operator fun set(v1: Double, v2: Double, value: Pair<Double, Double>) {
        val idx1 = ((v1 - start1) * step1).roundToInt().coerceIn(0 until size1)
        val idx2 = ((v2 - start2) * step2).roundToInt().coerceIn(0 until size2)

        table[idx1][idx2] = value
    }

    fun updateAll(f: (Double, Double, Pair<Double, Double>) -> Pair<Double, Double>) {
        for (i in 0 until size1) {
            val v1 = i / step1 + start1
            for(j in  0 until size2) {
                val v2 = j / step2 + start2
                table[i][j] = f(v1, v2, table[i][j])
            }
        }
    }

    fun iterate(f: (Double, Double, Pair<Double, Double>) -> Unit) {
        for (i in 0 until size1) {
            val v1 = i / step1 + start1
            for(j in  0 until size2) {
                val v2 = j / step2 + start2
                f(v1, v2, table[i][j])
            }
        }
    }

    fun write(path: String) {
        FileWriter(path).use {
            for (i in 0 until size1) {
                it.appendLine(table[i].map { it.second }.joinToString(","))
            }
        }
    }
}