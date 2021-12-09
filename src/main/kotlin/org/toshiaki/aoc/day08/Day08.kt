package org.toshiaki.aoc.day08

import org.toshiaki.aoc.Helper
import java.util.regex.Pattern

enum class numbers(val segment: String) {
    N0("abcefg"), // 6
    N1("cf"), // 2 *
    N2("acdeg"), // 5
    N3("acdfg"), // 5
    N4("bcdf"), // 4 *
    N5("abdfg"), // 5
    N6("abcefg"), // 6
    N7("acf"), // 3 *
    N8("abcdefg"), // 7 *
    N9("abcdfg"), // 6
}

fun main() {
    val inputLines = readInputLines("day08.txt")
    println(countNumbersInOutput(inputLines, listOf(numbers.N1, numbers.N4, numbers.N7, numbers.N8)))
}

fun countNumbersInOutput(inputLines: List<String>, numbersToFind: List<numbers>): Int {
    var count: Int = 0
    inputLines.forEach { line ->
        var outputSegments = line.substringAfter('|').split(Pattern.compile("\\s"))
        numbersToFind.forEach { toFind ->
            count += outputSegments.count { it.length == toFind.segment.length }
        }
    }
    return count
}

fun findAllMixedPatterns(mixedInput: List<String>): List<String> {
    val foundPatterns = mutableSetOf<String>()
    mixedInput.forEach { line ->
        val words = line.split(Pattern.compile("[|\\s]")).toSet()
        foundPatterns.addAll(words)
    }
    return foundPatterns.toList()
}
fun readInputLines(filename: String): List<String> {
    val lines = mutableListOf<String>()
    val file = Helper.readResourceFile(filename)
    file.bufferedReader().forEachLine {
        lines.add(it)
    }
    return lines.toList()
}
