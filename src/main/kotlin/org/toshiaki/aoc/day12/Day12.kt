package org.toshiaki.aoc.day12

import org.toshiaki.aoc.Helper
import java.io.File
import java.util.Stack

fun main() {
    val caveMap = readCaveLayout("day12.txt")
    println(findPathCount(caveMap, "start", "end", 1))
    println(findPathCount(caveMap, "start", "end", 2))
}

fun findPathCount(
    caveMap: Map<String, List<String>>,
    start: String,
    end: String,
    smallCaveMaxVisitCount: Int
): Long {
    val waysFound: MutableList<List<String>> = mutableListOf()
    val currentWay: Stack<String> = Stack()
    currentWay.add(start)
    findNextCave(currentWay, caveMap, end, waysFound, smallCaveMaxVisitCount)
//    waysFound.forEach { println(it) }
    return waysFound.size.toLong()
}

fun findNextCave(
    currentWay: Stack<String>,
    caveMap: Map<String, List<String>>,
    end: String,
    waysFound: MutableList<List<String>>,
    smallCaveMaxVisitCount: Int
) {
    val nextCaves = caveMap[currentWay.peek()]
    for (cave in nextCaves!!) {
        if (cave.elementAt(0).isLowerCase()) {
            if (cave == end) {
                val completeWay = currentWay.toMutableList()
                completeWay.add(cave)
                waysFound.add(completeWay)
                continue
            }
            if (cave == "start") {
                continue
            }
            if (smallCaveMaxVisitCount == 1 && currentWay.contains(cave)) {
                continue
            } else if (smallCaveMaxVisitCount == 2 && currentWay.contains(cave)) {
                val smallCaves = currentWay.filter { it.elementAt(0).isLowerCase() }.toList()
                val eachCount = smallCaves.groupingBy { it }.eachCount()
                if (eachCount.containsValue(smallCaveMaxVisitCount)) {
                    continue
                }
            }
        }
        currentWay.add(cave)
        findNextCave(currentWay, caveMap, end, waysFound, smallCaveMaxVisitCount)
        currentWay.pop()
    }
}

fun readCaveLayout(filename: String): MutableMap<String, MutableList<String>> {
    val file: File = Helper.readResourceFile(filename)
    val caveMap: MutableMap<String, MutableList<String>> = mutableMapOf()
    file.forEachLine { line ->
        val a = line.substringBefore("-")
        val b = line.substringAfter("-")
        fun addCaveToMap(a: String, b: String) {
            var listWays = caveMap[a]
            if (listWays == null) {
                caveMap[a] = mutableListOf()
            }
            caveMap[a]?.add(b)
        }
        addCaveToMap(a, b)
        addCaveToMap(b, a)
    }
    return caveMap
}

class Cave(
    val name: String,
    var ways: MutableList<Cave> = mutableListOf()
) {
    fun isSmall(): Boolean = name.elementAt(0).isLowerCase()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cave

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}
