package org.toshiaki.aoc.day13

import org.toshiaki.aoc.Helper
import java.util.regex.Pattern

fun main() {
    val origami: TransparentOrigami = readInstructions("day13.txt")
//    println(calculateTotalPoints(origami, 1))
    println(calculateTotalPoints(origami, null))
}

fun calculateTotalPoints(origami: TransparentOrigami, limit: Int?): Long {
    var pointSet = origami.pointsList.toSet()
//    printPoints(pointSet)
    for (step in 0 until origami.foldList.size) {
        if (step != null && step == limit) {
            break
        }
        val fold = origami.foldList[step]
        val direction = fold.substringBefore("=")
        val position = fold.substringAfter("=").toInt()
        var newSet = mutableSetOf<Point>()
        pointSet.forEach { newSet.add(it.fold(direction, position)) }
        pointSet = newSet
//        printPoints(pointSet)
    }
    printPoints(pointSet)
    return pointSet.size.toLong()
}

fun readInstructions(filename: String): TransparentOrigami {
    val file = Helper.readResourceFile(filename)

    val origami = TransparentOrigami()
    file.bufferedReader().forEachLine { line ->
        if (line.startsWith("fold")) {
            origami.foldList.add(line.removePrefix("fold along "))
        } else if (Pattern.matches("\\d+,\\d+", line)) {
            val x = line.substringBefore(",").toInt()
            val y = line.substringAfter(",").toInt()
            origami.pointsList.add(Point(x, y))
        }
    }
    return origami
}

class TransparentOrigami(
    var pointsList: MutableList<Point> = mutableListOf(),
    val foldList: MutableList<String> = mutableListOf(),
)
class Point(
    var x: Int,
    var y: Int
) {
    fun fold(direction: String, toFold: Int): Point {
        when (direction) {
            "x" -> return foldX(toFold)
            "y" -> return foldY(toFold)
        }
        return this
    }
    private fun foldX(toFold: Int): Point {
        if (x > toFold) {
            val newX = toFold - (x - toFold)
            return Point(newX, y)
        }
        return this
    }
    private fun foldY(toFold: Int): Point {
        if (y > toFold) {
            val newY = toFold - (y - toFold)
            return Point(x, newY)
        }
        return this
    }

    override fun toString(): String {
        return "(x=$x, y=$y)"
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

fun printPoints(pointSet: Set<Point>) {
    val topX = pointSet.map { it.x }.maxOrNull()!!
    val topY = pointSet.map { it.y }.maxOrNull()!!
    for (x in 0..topX) {
        var row: String = ""
        for (y in 0..topY) {
            if (pointSet.contains(Point(x,y))) {
                row += "# "
            } else {
                row += ". "
            }
        }
        println(row)
    }
    println("------")
}
