package org.toshiaki.aoc.day02

import org.toshiaki.aoc.Helper
import java.io.File

fun main(args: Array<String>) {
    val instructions = readCourse()
    println(findFinalPosition(instructions))
    println(findFinalAimedPosition(instructions))
}

fun findFinalPosition(instructions: MutableList<String>): Long {
    var depth: Long = 0
    var position: Long = 0

    instructions.forEach {
        val command = it.substringBefore(' ')
        val quantity = it.substringAfter(' ').toInt()
        when (command) {
            "forward" -> position += quantity
            "down" -> depth += quantity
            "up" -> depth -= quantity
        }
    }
    return depth * position
}

fun findFinalAimedPosition(instructions: MutableList<String>): Long {
    var depth: Long = 0
    var position: Long = 0
    var aim: Long = 0
    instructions.forEach {
        val command = it.substringBefore(' ')
        val quantity = it.substringAfter(' ').toInt()
        when (command) {
            "forward" -> {
                position += quantity
                depth += aim * quantity
            }
            "down" -> aim += quantity
            "up" -> aim -= quantity
        }
    }
    return depth * position
}

fun readCourse(): MutableList<String> {
    val file: File = Helper.readResourceFile("day02.txt")
    val instructions = mutableListOf<String>()
    file.bufferedReader().forEachLine {
        instructions.add(it)
    }
    return instructions
}
