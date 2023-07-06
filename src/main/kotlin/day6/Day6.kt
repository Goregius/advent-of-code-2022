package day6

import utils.readInput

private fun main() {
    val input = readInput(6)

    println(part1(input))
    println(part2(input))
}

private fun part1(input: String): Int = findStartOfMessageIndex(input, 4)
private fun part2(input: String): Int = findStartOfMessageIndex(input, 14)
private fun findStartOfMessageIndex(input: String, packetSize: Int): Int =
    input.windowedSequence(packetSize)
        .indexOfFirst { it.toSet().size == packetSize } + packetSize