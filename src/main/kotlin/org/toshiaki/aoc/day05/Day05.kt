package org.toshiaki.aoc.day05

import org.toshiaki.aoc.Helper
import java.util.regex.Pattern
import kotlin.math.absoluteValue

fun main() {
    val lines = readLines("day05.txt")
//    println(calculateUsedPoints(lines, 2, false))
    println(calculateUsedPoints(lines, 2, true))
}

fun calculateUsedPoints(lines: List<Line>, usedTimes: Int, useDiagonals: Boolean): Int {
    val heatMap = mutableMapOf<Point, Int>()
    lines.forEach { line ->
        line.pointsUsed(useDiagonals).forEach {
            heatMap[it] = heatMap[it]?.plus(1) ?: 1
        }
    }
    return heatMap.filterValues { it >= usedTimes }.count()
}

fun readLines(filename: String): List<Line> {
    val file = Helper.readResourceFile(filename)
    val lines = mutableListOf<Line>()
    file.bufferedReader().forEachLine {
        val pattern = Pattern.compile("(\\d+),(\\d+)\\s[->]+\\s(\\d+),(\\d+)")
        val matcher = pattern.matcher(it)
        if (matcher.find()) {
            val x1 = matcher.group(1)?.toInt() ?: 0
            val y1 = matcher.group(2)?.toInt() ?: 0
            val x2 = matcher.group(3)?.toInt() ?: 0
            val y2 = matcher.group(4)?.toInt() ?: 0
            lines.add(Line(Point(x1, y1), Point(x2, y2)))
        }
    }
    return lines
}

class Line(
    val point1: Point,
    val point2: Point
) {
    fun pointsUsed(useDiagonals: Boolean): List<Point> {
        val usedPoints = mutableListOf<Point>()
        if (point1.x == point2.x) {
            if (point1.y < point2.y) {
                for (y in point1.y..point2.y) {
                    usedPoints.add(Point(point1.x, y))
                }
            } else {
                for (y in point2.y..point1.y) {
                    usedPoints.add(Point(point1.x, y))
                }
            }
        } else if (point1.y == point2.y) {
            if (point1.x < point2.x) {
                for (x in point1.x..point2.x) {
                    usedPoints.add(Point(x, point1.y))
                }
            } else {
                for (x in point2.x..point1.x) {
                    usedPoints.add(Point(x, point1.y))
                }
            }
        } else if (useDiagonals) {
            var calcX = 0
            var calcY = 0
            calcX = if (point1.x <= point2.x) 1 else -1
            calcY = if (point1.y <= point2.y) 1 else -1
            var startX = point1.x
            var startY = point1.y
            val diff = (point1.x - point2.x).absoluteValue
            for (i in 0..diff) {
                usedPoints.add(Point(startX, startY))
                startX += calcX
                startY += calcY
            }
        }
        return usedPoints
    }
    override fun toString(): String {
        return "[$point1 -> $point2]"
    }
}

class Point(
    val x: Int,
    val y: Int
) {
    override fun toString(): String {
        return "$x,$y"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}
