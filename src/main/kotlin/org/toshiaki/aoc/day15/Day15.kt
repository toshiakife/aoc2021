package org.toshiaki.aoc.day15

import org.toshiaki.aoc.Helper

fun main() {
    val cavernMap = readRiskLevel("day15.txt")
//    println(findLeastRiskPathScore(cavernMap))
//    printCave(cavernMap)
    val largeCave = enlargeCave(cavernMap)
    println(findLeastRiskPathScore(largeCave))
//    printCave(largeCave)
}

fun findLeastRiskPathScore(cavernMap: Map<String, Int>): Long {
    val start = "0_0"
    val limit = cavernMap.keys.map { it.substringBefore("_").toInt() }.maxOf { it }
    val end = "${limit}_$limit"
    val distances: MutableMap<String, Long> = mutableMapOf()
    distances[start] = 0
    val settings = Settings(start, end, cavernMap, distances)
    val currentPath = mutableListOf<String>()
    settings.processingQueue.add(settings.start)
    settings.distancesToNode[settings.start] = 0
    process(settings, null)
    return settings.distancesToNode[end]!!
}

fun process(settings: Settings, previous: String?) {
    while (!settings.processingQueue.isEmpty()) {
        val current = settings.processingQueue.removeFirst()

        val nextNodes = findAdjacentKeys(current, settings)
        nextNodes.sortedBy { settings.cavernMap[it] }.forEach {
            val costToNext = settings.distancesToNode[current]!! + settings.cavernMap[it]!!
            val existingCost = settings.distancesToNode.getOrDefault(it, Long.MAX_VALUE)
            if (costToNext < existingCost) {
                if (it != settings.end) {
                    settings.processingQueue.add(it)
                }
                settings.distancesToNode[it] = costToNext
            }
        }
    }
}
fun findAdjacentKeys(key: String, settings: Settings): List<String> {
    val x = key.substringBefore("_").toInt()
    val y = key.substringAfter("_").toInt()
    val adjacentKeys = mutableListOf<String>()
    adjacentKeys.add("${x - 1}_$y")
    adjacentKeys.add("${x + 1}_$y")
    adjacentKeys.add("${x}_${y - 1}")
    adjacentKeys.add("${x}_${y + 1}")
    return adjacentKeys.filter { settings.cavernMap.containsKey(it) }
}

fun enlargeCave(cavernMap: Map<String, Int>): Map<String, Int> {
    val limitX = cavernMap.keys.map { it.substringBefore("_").toInt() }.maxOf { it }
    val limitY = cavernMap.keys.map { it.substringAfter("_").toInt() }.maxOf { it }
    val horizontalCaveMap = cavernMap.toMutableMap()
    cavernMap.forEach { (key, value) ->
        horizontalCaveMap[key] = value
    }
    for (horizontal in 1 until 5) {
        cavernMap.forEach { key, risk ->
            val x = key.substringBefore("_").toInt()
            val y = key.substringAfter("_").toInt()
            val newY = y + ((limitY + 1) * horizontal)
            var newRisk = cavernMap[key]!! + horizontal
            if (newRisk > 9) {
                newRisk -= 9
            }
            horizontalCaveMap["${x}_$newY"] = newRisk
        }
    }
    val newCaveMap = horizontalCaveMap.toMutableMap()
    for (vertical in 1 until 5) {
        horizontalCaveMap.entries.forEach { (key, risk) ->
            val x = key.substringBefore("_").toInt()
            val y = key.substringAfter("_").toInt()
            val newX = x + ((limitX + 1) * vertical)
            var newRisk = horizontalCaveMap[key]!! + vertical
            if (newRisk > 9) {
                newRisk -= 9
            }
            newCaveMap["${newX}_$y"] = newRisk
        }
    }
    return newCaveMap
}

fun readRiskLevel(filename: String): Map<String, Int> {
    val file = Helper.readResourceFile(filename)
    val cavernMap = mutableMapOf<String, Int>()
    var row = 0
    file.bufferedReader().forEachLine { line ->
        val columns = line.trim().toList().map { it.digitToInt() }.toList()
        columns.forEachIndexed { column, riskLevel ->
            val key = "${row}_$column"
            cavernMap[key] = riskLevel
        }
        row++
    }
    return cavernMap
}

class Settings(
    val start: String,
    val end: String,
    val cavernMap: Map<String, Int>,
    val distancesToNode: MutableMap<String, Long>,
    val processingQueue: MutableList<String> = mutableListOf(),
)

fun printCave(cavernMap: Map<String, Int>) {
    val limitX = cavernMap.keys.map { it.substringBefore("_").toInt() }.maxOf { it }
    val limitY = cavernMap.keys.map { it.substringAfter("_").toInt() }.maxOf { it }
    for (x in 0..limitX) {
        var row: String = ""
        for (y in 0..limitY) {
            val key = "${x}_$y"
            row += "${cavernMap[key]}"
        }
        println(row)
    }
    println("------")
}
