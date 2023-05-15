package org.matthewo.framework.purepursuit

import kotlin.math.hypot

open class Point(val x: Double, val y: Double) {

    constructor(x: Number, y: Number) : this(x.toDouble(), y.toDouble())

    fun add(point: Point) = Point(x + point.x, y + point.y)

    operator fun plus(point: Point) = add(point)

    fun subtract(point: Point) = Point(x - point.x, y - point.y)

    operator fun minus(point: Point) = subtract(point)

    fun distance(point: Point) = hypot(point.x - x, point.y - y)

    override fun toString(): String {
        return "(" + String.format("%.4f", x) + "," + String.format("%.4f", y) + ")"
    }
}
