package org.matthewo

import org.ejml.simple.SimpleMatrix
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.letsPlot
import kotlin.system.measureTimeMillis

val STEPS = (DURATION / DT).toInt()

fun main() {
    val optimizer = TrajectoryOptimizer()

    val table = optimizer.optimize()

    val plotter = Plotter()

    val actions = mutableListOf<Double>()
    val states = mutableListOf<PendulumState>()
    var state = PendulumState(0.0, 0.0)

    val time = measureTimeMillis {
        repeat(STEPS) {
            states += state
            var action = table[state.position, state.velocity].second
            action = 0.0
            actions += action
            val v = state.velocity
            state = state.update(action)
            if (v >= 0 && state.velocity < 0) {
                println(it * TIMESTEP)
            }
        }
    }
    println(STEPS)
    println(time)

    val data = mapOf<String, Any>(
        "Time" to (0 until STEPS).map { it * TIMESTEP },
        "Action" to actions,
        "Position" to states.map { it.position },
        "Velocity" to states.map { it.velocity }
    )

    val plots = mapOf(
        "Action" to letsPlot(data) + geomLine(
            color = "red",
            alpha = .3,
            size = 2.0,
        ) {
            x = "Time"
            y = "Action"
        },
        "Position" to letsPlot(data) + geomLine(
            color = "dark-green",
            alpha = .3,
            size = 2.0,
        ) {
            x = "Time"
            y = "Position"
        },
        "Velocity" to letsPlot(data) + geomLine(
            color = "blue",
            alpha = .3,
            size = 2.0,
        ) {
            x = "Time"
            y = "Velocity"
        }
    )

    plotter.plot(plots)
}