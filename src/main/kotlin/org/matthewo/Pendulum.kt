package org.matthewo

import org.matthewo.framework.purepursuit.Vector
import kotlin.math.pow
import kotlin.time.DurationUnit

val MOMENT_OF_INERTIA = MASS * LENGTH.pow(2)
val TIMESTEP = DT.toDouble(DurationUnit.SECONDS)

data class PendulumState(val position: Double, val velocity: Double) {

    val vector = Vector[LENGTH, position]

    fun update(action: Double): PendulumState {
        val torque = action + (vector cross GRAVITY * MASS)
        val accel = Math.toDegrees(torque / MOMENT_OF_INERTIA) - velocity * FRICTION
        val position = position + velocity * TIMESTEP + accel * TIMESTEP.pow(2) / 2.0
        val velocity = velocity + accel * TIMESTEP
        return PendulumState(position, velocity)
    }
}