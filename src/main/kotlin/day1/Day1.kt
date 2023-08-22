package day1

import utils.readInput

private fun main() {
    val input = readInput(1)
    println("test")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: String): Int {
    val foodPerElf = parseInput(input)

    val caloriesPerElf = foodPerElf.calculateCaloriesPerElf()

    return caloriesPerElf.max()
}

private fun part2(input: String): Int {
    val foodPerElf = parseInput(input)

    val caloriesPerElf = foodPerElf.calculateCaloriesPerElf()

    return caloriesPerElf.sortedDescending().take(3).sum()
}

private fun parseInput(input: String) = input
    .split("\r\n\r\n", "\n\n", "\r\r")
    .map { elfText ->
        elfText.lines().map { it.toInt() }
    }

private fun List<List<Int>>.calculateCaloriesPerElf() = map { it.sum() }
