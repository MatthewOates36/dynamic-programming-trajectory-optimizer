package org.matthewo

import org.matthewo.framework.purepursuit.Pose
import org.matthewo.framework.purepursuit.Vector
import kotlin.math.*

const val WHEELBASE_LENGTH = 0.15

fun Pose.update(steeringAngle: Double, velocity: Double, timeStep: Double): Pose {
    val backPose = subtract(Vector(WHEELBASE_LENGTH / 2.0, 0).rotate(heading))
    val radius = WHEELBASE_LENGTH * tan(Math.toRadians(90 - steeringAngle))
    val distance = velocity * timeStep
    val arcLength = distance / radius
    val heading = heading + Math.toDegrees(arcLength)
    val translation = Vector(radius * sin(arcLength), radius * (1 - cos(arcLength)))
    return Pose(backPose + translation.rotate(this.heading) + Vector(WHEELBASE_LENGTH / 2.0, 0).rotate(heading), heading)
}

fun main() {
    var pose = Pose(0.0, 0.0, 0.0)
    println(pose)
    for(i in 1..47) {
        pose = pose.update(0.0, 1.0, 0.01)
        println(pose)
    }
}
