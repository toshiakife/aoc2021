package org.toshiaki.aoc.day04

import org.toshiaki.aoc.Helper
import java.io.File
import java.util.regex.Pattern

fun main() {
    val bingo = readBingoInput("day04.txt")
    bingo.play()
}

fun readBingoInput(file: String): Bingo {
    val file: File = Helper.readResourceFile(file)
    val reader = file.bufferedReader()
    val numbers: String = reader.readLine()
    val bingo = Bingo(numbers.split(",").map(String::toInt))

    var currentBoard: BingoBoard? = null
    var rowNumber = 1
    while (reader.ready()) {
        val line = reader.readLine()
        if (line.isEmpty()) {
            if (currentBoard != null) {
                currentBoard.size = rowNumber - 1
                bingo.boards.add(currentBoard)
            }
            currentBoard = BingoBoard()
            rowNumber = 1
        } else if (currentBoard != null) {
            currentBoard.addLine(rowNumber, line)
            rowNumber++
        }
    }
    if (currentBoard != null) {
        currentBoard.size = rowNumber - 1
        bingo.boards.add(currentBoard)
    }
    return bingo
}

class Bingo(
    private val drawnNumbers: List<Int>,
    val boards: MutableList<BingoBoard> = mutableListOf()
) {
    fun play() {
        number@ for (number in drawnNumbers) {
//            println("Number: $number")
            val withoutBingo = boards.filter { !it.hasBingo() }
            for (board in withoutBingo) {
                board.chooseNumber(number)
                if (board.hasBingo()) {
                    println(board.winningScore(number))
                }
            }
        }
    }
}

class BingoBoard(
    private val boardMap: MutableMap<String, Digit> = mutableMapOf(),
    var size: Int = 0
) {
    fun addLine(rowNumber: Int, line: String) {
        line.trim()
            .split(Pattern.compile("\\s+"))
            .map { it.trim().toInt() }
            .forEachIndexed { index, value ->
                val key: String = "${rowNumber}_" + (index + 1)
                boardMap[key] = Digit(value)
            }
    }
    fun chooseNumber(number: Int) {
        row@ for (x in 1..size) {
            column@ for (y in 1..size) {
                val key = "${x}_$y"
                if (boardMap[key]?.number == number) {
                    boardMap[key]?.checked = true
                }
            }
        }
    }
    fun hasBingo(): Boolean {
        row@ for (x in 1..size) {
            var bingo = true
            column@ for (y in 1..size) {
                val key = "${x}_$y"
                if (boardMap[key]?.checked == false) {
                    bingo = false
                    break@column
                }
            }
            if (bingo) {
                return true
            }
        }
        column@ for (y in 1..size) {
            var bingo = true
            row@ for (x in 1..size) {
                val key = "${x}_$y"
                if (boardMap[key]?.checked == false) {
                    bingo = false
                    break@row
                }
            }
            if (bingo) {
                return true
            }
        }
        return false
    }

    fun winningScore(lastNumber: Int): Long {
        var sum: Long = 0
        row@ for (x in 1..size) {
            column@ for (y in 1..size) {
                val key = "${x}_$y"
                if (boardMap[key]?.checked == false) {
                    sum += boardMap[key]?.number ?: 0
                }
            }
        }
        return sum * lastNumber
    }

    override fun toString(): String {
        val printer = StringBuilder()
        row@ for (x in 1..size) {
            column@ for (y in 1..size) {
                val key = "${x}_$y"
                printer.append(boardMap[key])
                printer.append(" ")
            }
            printer.append("\n")
        }
        return printer.toString()
    }
}

class Digit(
    val number: Int,
    var checked: Boolean = false
) {
    override fun toString(): String {
        val stg = String.format("%2d", number)
        if (checked) {
            return "*$stg*"
        }
        return " $stg "
    }
}
