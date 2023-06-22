package day6

fun part1(input: String): Int = findStartOfMessageIndex(input, 4)
fun part2(input: String): Int = findStartOfMessageIndex(input, 14)
private fun findStartOfMessageIndex(input: String, packetSize: Int): Int =
    input.windowedSequence(packetSize)
        .indexOfFirst { it.toSet().size == packetSize } + packetSize