package org.toshiaki.aoc.day10

import org.toshiaki.aoc.Helper
import java.util.Stack

fun main() {
    val lines: List<String> = readInput("day10.txt")
    println(findSyntaxErrorScore(lines))
    println(findMiddleIncompleteScore(lines))
}

fun findSyntaxErrorScore(lines: List<String>): Long {
    var scoreSum: Long = 0
    for (line in lines) {
        val corruptedChar = findCorruptedChar(line)

        val score = when (corruptedChar) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> 0
        }
        scoreSum += score
    }
    return scoreSum
}

fun findMiddleIncompleteScore(lines: List<String>): Long {
    var scoreList: MutableList<Long> = mutableListOf()
    for (line in lines) {
        var score: Long = 0
        val missingPairs = findIncompleteStack(line)
        while (!missingPairs.empty()) {
            score *= 5
            when (missingPairs.pop()) {
                '(' -> score += 1
                '[' -> score += 2
                '{' -> score += 3
                '<' -> score += 4
                else -> score += 0
            }
        }
        if (score > 0) {
            scoreList.add(score)
        }
    }
    val middleIndex: Int = (scoreList.size / 2) - 1 + scoreList.size % 2
    return scoreList.sorted()[middleIndex]
}

fun findCorruptedChar(line: String): Char {
    fun isCorrupted(lastOpened: Char, toCheck: Char): Boolean {
        val expected: Char = when (lastOpened) {
            '(' -> ')'
            '[' -> ']'
            '{' -> '}'
            '<' -> '>'
            else -> '-'
        }
        return expected != toCheck
    }
    val chunks: Stack<Char> = Stack()
    line.forEach {
        when (it) {
            '(', '[', '{', '<' -> chunks.add(it)
            else -> {
                if (isCorrupted(chunks.pop(), it)) {
                    return it
                }
            }
        }
    }
    return '-'
}

fun findIncompleteStack(line: String): Stack<Char> {
    fun isCorrupted(lastOpened: Char, toCheck: Char): Boolean {
        val expected: Char = when (lastOpened) {
            '(' -> ')'
            '[' -> ']'
            '{' -> '}'
            '<' -> '>'
            else -> '-'
        }
        return expected != toCheck
    }
    val chunks: Stack<Char> = Stack()
    line.forEach {
        when (it) {
            '(', '[', '{', '<' -> chunks.add(it)
            else -> {
                if (isCorrupted(chunks.pop(), it)) {
                    return Stack()
                }
            }
        }
    }
    return chunks
}

fun readInput(filename: String): List<String> {
    val file = Helper.readResourceFile(filename)
    return file.bufferedReader().lines().toList()
}
class Chunk(
    val innerChunks: MutableList<Chunk> = mutableListOf<Chunk>(),
    val type: ChunkType,
) {
    override fun toString(): String {
        var children = ""
        innerChunks.forEach { children += it.toString() }
        return "${type.start}$children${type.end}"
    }
}

enum class ChunkType(
    val start: Char,
    val end: Char
) {
    PARENTHESES('(', ')'),
    BRACKETS('[', ']'),
    BRACES('{', '}'),
    CHEVRONS('<', '>')
}
