package org.toshiaki.aoc.day14

import org.toshiaki.aoc.Helper

fun main() {
    val formula = readInstructions("day14.txt")
    println(findDiff(formula, 40))
}

fun findDiff(formula: PolymerFormula, steps: Int): Long {
    var template = formula.template
    println(template)
    var pairMap: MutableMap<String, Long> = mutableMapOf()
    for (i in 0 until (template.length - 1)) {
        val polymer = "${template[i]}${template[i + 1]}"
        pairMap[polymer] = pairMap.getOrDefault(polymer, 0) + 1
    }
    for (step in 1..steps) {
        val newPairMap = mutableMapOf<String, Long>()
        pairMap.forEach { (polymer, count) ->
            val polymerA = polymer[0] + formula.rules[polymer]!!
            val polymerB = formula.rules[polymer]!! + polymer[1]
            newPairMap[polymerA] = newPairMap.getOrDefault(polymerA, 0) + count
            newPairMap[polymerB] = newPairMap.getOrDefault(polymerB, 0) + count
        }
        pairMap = newPairMap
    }
    val letterCount: MutableMap<Char, Long> = mutableMapOf()
    pairMap.forEach { (polimer, count) ->
        letterCount[polimer.first()] = letterCount.getOrDefault(polimer.first(), 0) + count
    }
    letterCount[template.last()] = letterCount.getOrDefault(template.last(), 0) + 1
    val sorted = letterCount.toList().sortedBy { (_, count) -> count }
    return sorted.last().second - sorted.first().second
}

fun readInstructions(filename: String): PolymerFormula {
    val file = Helper.readResourceFile(filename)
    val pairs: MutableList<String> = mutableListOf()
    var template: String = ""
    for (line in file.bufferedReader().lines()) {
        if (line.isEmpty()) continue
        if (line.contains("->")) {
            pairs.add(line)
        } else {
            template = line
        }
    }
    val pairMap = pairs.map { it.substringBefore("->").trim() to it.substringAfter("->").trim() }.toMap()
    return PolymerFormula(template, pairMap)
}

class PolymerFormula(
    val template: String,
    val rules: Map<String, String>
)
