package org.toshiaki.aoc.day08

import org.toshiaki.aoc.Helper
import java.util.regex.Pattern

fun main() {
    val inputLines = readInputLines("day08.txt")
    println(countNumbersInOutput(inputLines, listOf(Numbers.N1, Numbers.N4, Numbers.N7, Numbers.N8)))
    println(calculateOutputSum(inputLines))
}

fun findOutputValue(line: String): Long {
    val mixedPatterns = findAllMixedPatterns(line)
    val charMap: Map<Char, Char> = findCharMap(mixedPatterns)
    val output = line.substringAfter('|')
    val outputList = output.trim().split(" ")
    var outputStringValue = ""
    outputList.forEach { encoded ->
        var decoded = encoded.toList().map { charMap[it] }.joinToString("")
        decoded = decoded.toList().sorted().joinToString("")
        outputStringValue += Numbers.values().first { it.segment == decoded }.number
    }
    return outputStringValue.toLong()
}

fun calculateOutputSum(mixedInput: List<String>): Long {
    var sum: Long = 0
    mixedInput.forEach { line ->
        sum += findOutputValue(line)
    }
    return sum
}

enum class Numbers(val number: Int, val segment: String) {
    N1(1, "cf"), // 2 *
    N7(7, "acf"), // 3 *
    N4(4, "bcdf"), // 4 *
    N8(8, "abcdefg"), // 7 *
    N9(9, "abcdfg"), // 6
    N2(2, "acdeg"), // 5
    N3(3, "acdfg"), // 5

    N0(0, "abcefg"), // 6
    N5(5, "abdfg"), // 5
    N6(6, "abdefg"), // 6
}

fun findCharMap(mixedPatterns: List<String>): Map<Char, Char> {
    val one = mixedPatterns.first { it.length == 2 }
    val seven = mixedPatterns.first { it.length == 3 }
    val four = mixedPatterns.first { it.length == 4 }
    val eight = mixedPatterns.first { it.length == 7 }
    val nine = mixedPatterns.first { it.length == 6 && it.toList().containsAll(four.toList()) }

    val a: Char = seven.first { !one.contains(it) }
    val e: Char = eight.toList().first { !nine.contains(it) }

    val two = mixedPatterns.first { it.length == 5 && it.contains(e) }
    val three = mixedPatterns.first { it.length == 5 && it.toList().containsAll(seven.toList()) }

    val f: Char = three.toList().first { !two.contains(it) }
    val g: Char = three.toList().first { !four.contains(it) && it != a && it != f }
    val d: Char = three.toList().first { it != g && !seven.contains(it) }
    val b: Char = four.toList().first { it != d && !one.contains(it) }
    val c: Char = one.toList().first { it != f }

    val charMap = mutableMapOf<Char, Char>()
    charMap[a] = 'a'
    charMap[b] = 'b'
    charMap[c] = 'c'
    charMap[d] = 'd'
    charMap[e] = 'e'
    charMap[f] = 'f'
    charMap[g] = 'g'
    return charMap
}

fun findAllMixedPatterns(mixedInputLine: String): List<String> {
    val foundPatterns = mutableSetOf<String>()
    var words = mixedInputLine.substringBefore('|').trim().split(" ").toList()
    words = words.map { it.toList().sorted().joinToString("") }
    foundPatterns.addAll(words)
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

fun countNumbersInOutput(inputLines: List<String>, numbersToFind: List<Numbers>): Int {
    var count: Int = 0
    inputLines.forEach { line ->
        var outputSegments = line.substringAfter('|').split(Pattern.compile("\\s"))
        numbersToFind.forEach { toFind ->
            count += outputSegments.count { it.length == toFind.segment.length }
        }
    }
    return count
}
