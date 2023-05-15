package org.matthewo

import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.letsPlot
import org.matthewo.framework.purepursuit.Pose
import org.matthewo.framework.purepursuit.Vector
import org.matthewo.math.CarLookAheadTrajectory
import kotlin.math.*

const val WHEELBASE_LENGTH = 0.15

fun Pose.update(steeringAngle: Double, velocity: Double, timestep: Double): Pose {
    val backPose = subtract(Vector(WHEELBASE_LENGTH / 2.0, 0).rotate(heading))
    val radius = WHEELBASE_LENGTH * tan(Math.toRadians(90 - steeringAngle))
    val distance = velocity * timestep
    val arcLength = distance / radius
    val heading = heading + Math.toDegrees(arcLength)
    val translation = Vector(radius * sin(arcLength), radius * (1 - cos(arcLength)))
    return Pose(backPose + translation.rotate(this.heading) + Vector(WHEELBASE_LENGTH / 2.0, 0).rotate(heading), heading)
}

fun main() {
//    var pose = Pose(0.0, 0.0, 0.0)
//    println(pose)
//    for(i in 1..47) {
//        pose = pose.update(0.0, 1.0, 0.01)
//        println(pose)
//    }

    val plotter = Plotter()

    val lookAheadPoses = mutableListOf<Pose>()
    val optimizerPoses = mutableListOf<Pose>()
    val optimizer = CarTrajectoryOptimizer()
    val table = optimizer.optimize()
    val lookAhead = CarLookAheadTrajectory()


    val initialPose = Pose(0.0, 0.8, 45.0)
    var lookAheadPose = initialPose
    var optimizerPose = initialPose

    val velocity = 1.0
    val timestep = 0.001

    repeat (5000) {
        lookAheadPoses += lookAheadPose
        optimizerPoses += optimizerPose
        val lookAheadAction = lookAhead.calculate(lookAheadPose)
        val (_, optimizerAction) = table[optimizerPose.heading, optimizerPose.y]
        lookAheadPose = lookAheadPose.update(lookAheadAction, velocity, timestep)
        optimizerPose = optimizerPose.update(optimizerAction, velocity, timestep)
    }

    val data = mapOf<String, Any>(
//        "Time" to (0 until STEPS).map { it * TIMESTEP },
        "Look Ahead X" to lookAheadPoses.map { it.x },
        "Look Ahead Y" to lookAheadPoses.map { it.y },
        "Optimized X" to optimizerPoses.map { it.x },
        "Optimized Y" to optimizerPoses.map { it.y },
    )

    val plots = mapOf(
//        "Action" to letsPlot(data) + geomLine(
//            color = "red",
//            alpha = .3,
//            size = 2.0,
//        ) {
//            x = "Time"
//            y = "Action"
//        },
        "Poses" to letsPlot(data) + geomLine(
            color = "red",
            alpha = .6,
            size = 2.0,
        ) {
            x = "Look Ahead X"
            y = "Look Ahead Y"
        } + geomLine(
            color = "blue",
            alpha = .6,
            size = 2.0,
        ) {
            x = "Optimized X"
            y = "Optimized Y"
        },
//        "Velocity" to letsPlot(data) + geomLine(
//            color = "blue",
//            alpha = .3,
//            size = 2.0,
//        ) {
//            x = "Time"
//            y = "Velocity"
//        },
    )

    plotter.plot(plots)
}
