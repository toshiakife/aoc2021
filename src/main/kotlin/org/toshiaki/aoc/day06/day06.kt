package org.toshiaki.aoc.day06

val testInput = "3,4,3,1,2"
val input = "4,1,1,4,1,2,1,4,1,3,4,4,1,5,5,1,3,1,1,1,4,4,3,1,5,3,1,2,5,1,1,5,1,1,4,1,1,1,1,2,1,5,3,4,4,1,1,1,1,1,1,1,1,1,2,1,1,1,1,1,5,1,1,1,4,1,2,3,5,1,2,2,4,1,4,4,4,1,2,5,1,2,1,1,1,1,1,1,4,1,1,4,3,4,2,1,3,1,1,1,3,5,5,4,3,4,1,5,1,1,1,2,2,1,3,1,2,4,1,1,3,3,1,3,3,1,1,3,1,5,1,1,3,1,1,1,5,4,1,1,1,1,4,1,1,3,5,4,3,1,1,5,4,1,1,2,5,4,2,1,4,1,1,1,1,3,1,1,1,1,4,1,1,1,1,2,4,1,1,1,1,3,1,1,5,1,1,1,1,1,1,4,2,1,3,1,1,1,2,4,2,3,1,4,1,2,1,4,2,1,4,4,1,5,1,1,4,4,1,2,2,1,1,1,1,1,1,1,1,1,1,1,4,5,4,1,3,1,3,1,1,1,5,3,5,5,2,2,1,4,1,4,2,1,4,1,2,1,1,2,1,1,5,4,2,1,1,1,2,4,1,1,1,1,2,1,1,5,1,1,2,2,5,1,1,1,1,1,2,4,2,3,1,2,1,5,4,5,1,4"

fun main() {
    println(calculateFish(input, 256))
}

fun calculateFish(testInput: String, days: Int): Long {
    var allFish: MutableList<Int> = testInput.split(',')
        .map(String::toInt)
        .toMutableList()
    var mapFishQuantity = mutableMapOf<Int, Long>()
    for (day in 0..8) {
        val count = allFish.filter { it == day }.count()
        mapFishQuantity[day] = count.toLong()
    }
    for (day in 1..days) {
        val nextDay = mutableMapOf<Int, Long>()
        mapFishQuantity.toSortedMap().forEach { day, count ->
            val alreadyAdded: Long = if (nextDay.contains(day - 1)) nextDay[day - 1]!! else 0
            if (day == 0) {
                nextDay[8] = count
                nextDay[6] = count
            } else {
                nextDay[day - 1] = count + alreadyAdded
            }
        }
        mapFishQuantity = nextDay
    }
    var countAllFish: Long = 0
    mapFishQuantity.forEach { countAllFish += it.value }
    return countAllFish
}
