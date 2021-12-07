package org.toshiaki.aoc.day03

import org.toshiaki.aoc.Helper
import java.io.File

fun main() {
    val bitList = readDiagnosticReport()
    println(calculatePowerConsumption(bitList))
    println(calculateOxygenAndCo2Rate(bitList))
}

fun calculatePowerConsumption(bitList: MutableList<String>): Long {
    if (bitList == null || bitList.isEmpty()) {
        return 0
    }
    val bitOneCountList: MutableList<Int> = mutableListOf()
    val bitZeroCountList: MutableList<Int> = mutableListOf()
    val bitCount: Int = bitList[0].length
    for (i in 0 until bitCount) {
        bitOneCountList.add(0)
        bitZeroCountList.add(0)
    }
    bitList.forEach {
        it.forEachIndexed { index, c ->
            when (c) {
                '0' -> bitZeroCountList[index]++
                '1' -> bitOneCountList[index]++
            }
        }
    }
    var gamaRate: String = ""
    var epsilonRate: String = ""
    for (i in 0 until bitCount) {
        if (bitOneCountList[i] > bitZeroCountList[i]) {
            gamaRate += '1'
            epsilonRate += '0'
        } else {
            gamaRate += '0'
            epsilonRate += '1'
        }
    }
    return gamaRate.toLong(2) * epsilonRate.toLong(2)
}

fun calculateOxygenAndCo2Rate(bitList: MutableList<String>): Long {
    if (bitList == null || bitList.isEmpty()) {
        return 0
    }
    val bitOneCountList: MutableList<Int> = mutableListOf()
    val bitZeroCountList: MutableList<Int> = mutableListOf()
    val bitCount: Int = bitList[0].length
    for (i in 0 until bitCount) {
        bitOneCountList.add(0)
        bitZeroCountList.add(0)
    }
    bitList.forEach {
        it.forEachIndexed { index, c ->
            when (c) {
                '0' -> bitZeroCountList[index]++
                '1' -> bitOneCountList[index]++
            }
        }
    }
    var oxygenRateList = bitList
    var co2RateList = bitList
    var getMostCommonBit: (Int, MutableList<Int>) -> Char = {
        index: Int, scoreList: MutableList<Int> ->
        when (scoreList[index]) {
            1 -> '1'
            0 -> '0'
            else -> '1'
        }
    }
    var getLeastCommonBit: (Int, MutableList<Int>) -> Char = {
        index: Int, scoreList: MutableList<Int> ->
        when (scoreList[index]) {
            1 -> '0'
            0 -> '1'
            else -> '0'
        }
    }
    for (i in 0 until bitCount) {
        if (oxygenRateList.size > 1) {
            val oxygenBitScore = getBitScoreMap(oxygenRateList)
            oxygenRateList = oxygenRateList.filter { it[i] == getMostCommonBit(i, oxygenBitScore) }.toMutableList()
        }
        if (co2RateList.size > 1) {
            val co2BitScore = getBitScoreMap(co2RateList)
            co2RateList = co2RateList.filter { it[i] == getLeastCommonBit(i, co2BitScore) }.toMutableList()
        }
    }
    return oxygenRateList[0].toLong(2) * co2RateList[0].toLong(2)
}
fun getBitScoreMap(bitList: MutableList<String>): MutableList<Int> {
    val bitOneCountList: MutableList<Int> = mutableListOf()
    val bitZeroCountList: MutableList<Int> = mutableListOf()
    val bitCount: Int = bitList[0].length
    for (i in 0 until bitCount) {
        bitOneCountList.add(0)
        bitZeroCountList.add(0)
    }
    bitList.forEach {
        it.forEachIndexed { index, c ->
            when (c) {
                '0' -> bitZeroCountList[index]++
                '1' -> bitOneCountList[index]++
            }
        }
    }
    val bitScoreMap: MutableList<Int> = mutableListOf()
    for (i in 0 until bitCount) {
        var diff = bitOneCountList[i] - bitZeroCountList[i]
        var score = when {
            diff > 0 -> 1
            diff < 0 -> 0
            else -> -1
        }
        bitScoreMap.add(score)
    }
    return bitScoreMap
}

fun readDiagnosticReport(): MutableList<String> {
    val file: File = Helper.readResourceFile("day03.txt")
    val bitList = mutableListOf<String>()
    file.bufferedReader().forEachLine {
        bitList.add(it)
    }
    return bitList
}
