package org.matthewo.framework.purepursuit

import kotlin.math.*

class Vector(x: Double = 0.0, y: Double = 0.0) : Point(x, y) {

    val magnitude: Double by lazy { hypot(x, y) }
    val angle: Double by lazy { Math.toDegrees(atan2(y, x)) }

    constructor(x: Number, y: Number) : this(x.toDouble(), y.toDouble())

    constructor(point: Point) : this(point.x, point.y)

    constructor(point1: Point, point2: Point) : this(point2 - point1)

    constructor(x1: Double, y1: Double, x2: Double, y2: Double) : this(Point(x1, y1), Point(x2, y2))

    fun normalize() = Vector[1.0, angle]

    fun scale(scalar: Double) = Vector[magnitude * scalar, angle]

    operator fun times(scalar: Double) = scale(scalar)

    operator fun div(scalar: Double) = Vector[magnitude / scalar, angle]

    fun rotate(degrees: Double): Vector = Vector[magnitude, angle + degrees]

    infix fun dot(vector: Vector) = x * vector.x + y * vector.y

    infix fun cross(vector: Vector) = x * vector.y - vector.x * y

    override fun toString() = "<$x,$y>"

    companion object {

        fun polar(magnitude: Double, angle: Double) = Math.toRadians(angle).let { Vector(magnitude * cos(it), magnitude * sin(it)) }

        operator fun get(magnitude: Double, direction: Double) = polar(magnitude, direction)

        operator fun get(magnitude: Number, direction: Number) = polar(magnitude.toDouble(), direction.toDouble())
    }
}