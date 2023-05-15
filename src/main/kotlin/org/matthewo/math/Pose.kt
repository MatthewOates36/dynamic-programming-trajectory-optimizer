package org.matthewo.framework.purepursuit

class Pose(x: Double = 0.0, y: Double = 0.0, heading: Double = 0.0) : Point(x, y) {

    val heading: Double by lazy {
        normalizeAngle(heading)
    }

    constructor(point: Point, heading: Double) : this(point.x, point.y, heading)

    override fun toString() = "x: $x y: $y heading: $heading"

    companion object {

        fun normalizeAngle(angle: Double): Double = (((angle) % 360) + 540) % 360 - 180
    }
}
