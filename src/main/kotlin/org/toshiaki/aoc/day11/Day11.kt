package org.toshiaki.aoc.day11

import org.toshiaki.aoc.Helper

fun main() {
    val cavernMap: MutableMap<String, Int> = readCavernMap("day11.txt")
//    println(findFlashCount(cavernMap, 100))
    println(findStepAllFlash(cavernMap))
}

fun findFlashCount(cavernMap: MutableMap<String, Int>, steps: Int): Long {
    var flashCount: Long = 0
    printCavern("0", cavernMap)
    for (step in 1..steps) {
        var flashedInStep: MutableList<String> = mutableListOf()
        cavernMap.filter { (_, value) -> value <= 9 }.forEach { (key, energy) -> cavernMap[key] = energy + 1 }
        flashOctopuses(cavernMap, flashedInStep)
        flashCount += flashedInStep.size
        flashedInStep.forEach { cavernMap[it] = 0 }
        printCavern(step.toString(), cavernMap)
    }
    return flashCount
}

fun findStepAllFlash(cavernMap: MutableMap<String, Int>): Long {
    printCavern("0", cavernMap)
    var step: Int = 1
    while (true) {
        var flashedInStep: MutableList<String> = mutableListOf()
        cavernMap.filter { (_, value) -> value <= 9 }.forEach { (key, energy) -> cavernMap[key] = energy + 1 }
        flashOctopuses(cavernMap, flashedInStep)
        flashedInStep.forEach { cavernMap[it] = 0 }
        printCavern(step.toString(), cavernMap)
        if (flashedInStep.size == cavernMap.size)
            return step.toLong()
        step++
    }
    return 0
}

fun flashOctopuses(cavernMap: MutableMap<String, Int>, flashedInStep: MutableList<String>) {
    val flashedNow = cavernMap.filter { (key, value) -> value > 9 && !flashedInStep.contains(key) }.keys.toList()
    if (flashedNow.isEmpty()) {
        return
    }
    flashedInStep.addAll(flashedNow)
    flashedNow.forEach { key ->
        val adjacentKeys: List<String> = findAdjacentKeys(key, flashedInStep, cavernMap)
        cavernMap.filter { (key, value) -> adjacentKeys.contains(key) && value <= 9 }
            .forEach { (key, value) -> cavernMap[key] = value + 1 }
    }
    flashOctopuses(cavernMap, flashedInStep)
    return
}

fun findAdjacentKeys(key: String, alreadyFlashed: List<String>, cavernMap: MutableMap<String, Int>): List<String> {
    val x = key.substringBefore("_").toInt()
    val y = key.substringAfter("_").toInt()
    val adjacentKeys = mutableListOf<String>()
    adjacentKeys.add("${x - 1}_$y") // left
    adjacentKeys.add("${x + 1}_$y") // right
    adjacentKeys.add("${x}_${y - 1}") // top
    adjacentKeys.add("${x}_${y + 1}") // bottom
    adjacentKeys.add("${x - 1}_${y - 1}") // top left
    adjacentKeys.add("${x + 1}_${y - 1}") // top right
    adjacentKeys.add("${x - 1}_${y + 1}") // bottom left
    adjacentKeys.add("${x + 1}_${y + 1}") // bottom right
    return adjacentKeys.filter { cavernMap.containsKey(it) && !alreadyFlashed.contains(it) }
}

fun readCavernMap(filename: String): MutableMap<String, Int> {
    val file = Helper.readResourceFile(filename)
    val cavernMap = mutableMapOf<String, Int>()
    var row = 0
    file.bufferedReader().forEachLine { line ->
        val columns = line.trim().toList().map { it.digitToInt() }.toList()
        columns.forEachIndexed { column, energy ->
            val key = "${row}_$column"
            cavernMap[key] = energy
        }
        row++
    }
    return cavernMap
}

fun printCavern(step: String, cavernMap: MutableMap<String, Int>) {
    println("- $step")
    for (x in 0..9) {
        var row = ""
        for (y in 0..9) {
            val i = cavernMap.getValue("${x}_$y")
            if (i > 9) {
                row += "* "
            } else {
                row += "$i "
            }
        }
        println(row)
    }
    println("-")
}
