package org.matthewo.math

import org.matthewo.framework.purepursuit.Pose
import org.matthewo.framework.purepursuit.Vector
import kotlin.math.abs
import kotlin.math.cos

class CarLookAheadTrajectory {

    val LOOKAHEAD_DISTANCE = 0.4

    fun calculate(pose: Pose): Double {
        val lookAheadPoint = pose + Vector(LOOKAHEAD_DISTANCE, 0).rotate(pose.heading)
        val l = lookAheadPoint.y / cos(Math.toRadians(abs(pose.heading)))
        return (-20 * l).coerceIn(-27.0..27.0)
    }
}