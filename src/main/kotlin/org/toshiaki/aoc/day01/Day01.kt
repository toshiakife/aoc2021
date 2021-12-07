package org.toshiaki.aoc

fun main() {
    val file = Helper.readResourceFile("day01.txt")
    val measurements = mutableListOf<Int>()
    file.forEachLine {
        measurements.add(it.toInt())
    }

    println("Measurement increase count: " + countMeasurementIncreases(measurements))
    println("Group increase count: " + countGroupIncreases(measurements))
}

fun countMeasurementIncreases(measurements: MutableList<Int>): Int {
    var countIncreases = 0
    var previousMeasurement: Int? = null
    measurements.forEach {
        if (previousMeasurement != null && it > previousMeasurement!!) {
            countIncreases++
        }
        previousMeasurement = it
    }
    return countIncreases
}

fun countGroupIncreases(measurements: MutableList<Int>): Int {
    var countIncreases = 0
    var previousGroupValue: Int? = null
    for (i in measurements.indices) {
        val actualGroupValue: Int? = getGroupValue(measurements, i)
        if (actualGroupValue != null && previousGroupValue != null && actualGroupValue > previousGroupValue) {
            countIncreases++
        }
        previousGroupValue = actualGroupValue
    }
    return countIncreases
}
fun getGroupValue(measurements: MutableList<Int>, index: Int): Int? {
    if (index < 2) return null
    return measurements[index] + measurements[index - 1] + measurements[index - 2]
}
