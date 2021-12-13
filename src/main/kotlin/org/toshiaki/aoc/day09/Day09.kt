package org.toshiaki.aoc.day09

import org.toshiaki.aoc.Helper

fun main() {
    val heightMap = readHeightMap("day09.txt")
//    println(findLowPointsRiskLevel(heightMap))
    println(findTop3BasinsValue(heightMap))
}

fun findTop3BasinsValue(heightMap: MutableMap<String, Int>): Long {
    var basinList: MutableList<List<String>> = mutableListOf()
    var visited: MutableList<String> = mutableListOf()
    for (key in heightMap.keys) {
        val currentBasin = addSameBasin(key, mutableListOf<String>(), heightMap, visited)
        if (currentBasin.isNotEmpty()) {
            basinList.add(currentBasin)
        }
    }
    basinList.sortByDescending { findBasinValue(it, heightMap) }
    val top3 = basinList.filterIndexed { index, list -> index <= 2 }
    var top3Value: Long = 1
    top3.forEach { top3Value *= it.size }
    return top3Value
}

fun findBasinValue(basin: List<String>, heightMap: MutableMap<String, Int>): Long {
    return basin.sumOf { heightMap.getValue(it) }.toLong()
}

fun addSameBasin(key: String, basin: MutableList<String>, heightMap: MutableMap<String, Int>, visited: MutableList<String>): List<String> {
    if (visited.contains(key) || !heightMap.containsKey(key)) {
        return basin
    }
    if (heightMap[key] == 9) {
        visited.add(key)
        return basin
    }
    visited.add(key)
    basin.add(key)
    val adjacents = findAdjacentKeys(key)
    for (adjacent in adjacents) {
        addSameBasin(adjacent, basin, heightMap, visited)
    }
    return basin
}
fun findLowPointsRiskLevel(heightMap: MutableMap<String, Int>): Long {
    val lowPoints = mutableListOf<Int>()
    heightMap.forEach { key, height ->
        val adjacentKeys: List<String> = findAdjacentKeys(key)
        if (isLowerThanAdjacents(adjacentKeys, height, heightMap)) {
            lowPoints.add(height)
        }
    }
    return lowPoints.map { it + 1 }.sum().toLong()
}

fun isLowerThanAdjacents(adjacentKeys: List<String>, height: Int, heightMap: MutableMap<String, Int>): Boolean {
    for (adjacentKey in adjacentKeys) {
        val adjacentValue = heightMap[adjacentKey]
        if (adjacentValue != null && adjacentValue <= height) {
            return false
        }
    }
    return true
}

fun findAdjacentKeys(key: String): List<String> {
    val x = key.substringBefore("_").toInt()
    val y = key.substringAfter("_").toInt()
    val adjacentKeys = mutableListOf<String>()
    adjacentKeys.add("${x - 1}_$y")
    adjacentKeys.add("${x + 1}_$y")
    adjacentKeys.add("${x}_${y - 1}")
    adjacentKeys.add("${x}_${y + 1}")
    return adjacentKeys
}

fun readHeightMap(filename: String): MutableMap<String, Int> {
    val file = Helper.readResourceFile(filename)
    val heightMap = mutableMapOf<String, Int>()
    var row = 0
    file.bufferedReader().forEachLine { line ->
        val columns = line.trim().toList().map { it.digitToInt() }.toList()
        columns.forEachIndexed { column, height ->
            val key = "${row}_$column"
            heightMap[key] = height
        }
        row++
    }
    return heightMap
}
