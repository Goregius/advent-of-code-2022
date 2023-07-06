package day4

import utils.readInput

private fun main() {
    val input = readInput(4)

    println(part1(input))
    println(part2(input))
}

private fun part1(input: String): Int = input.lines().count { line ->
    val (firstSections, secondSections) = parseLine(line)

    firstSections.first in secondSections &&
            firstSections.last in secondSections ||
            secondSections.first in firstSections &&
            secondSections.last in firstSections
}

private fun part2(input: String): Int = input.lines().count { line ->
    val (firstSections, secondSections) = parseLine(line)

    firstSections.any { it in secondSections }
}

private fun parseLine(line: String): List<IntRange> = line
    .split(",")
    .map { rangeText ->
        val rangeList = rangeText.split("-").map { it.toInt() }
        (rangeList[0]..rangeList[1])
    }