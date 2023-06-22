package day5

fun part1(input: String): String {
    val (stacks, steps) = parseInput(input)
    val stacksCopy = stacks.map { ArrayDeque(it) }

    for (step in steps) {
        repeat(step.moves) {
            val crate = stacksCopy[step.originStack - 1].removeFirst()
            stacksCopy[step.destinationStack - 1].addFirst(crate)
        }
    }

    return stacksToAnswer(stacksCopy)
}

fun part2(input: String): String {
    val (stacks, steps) = parseInput(input)
    val stacksCopy = stacks.map { ArrayDeque(it) }

    for (step in steps) {
        val removedCrates = stacksCopy[step.originStack - 1].removeFirst(step.moves)
        stacksCopy[step.destinationStack - 1].addAll(0, removedCrates)
    }

    return stacksToAnswer(stacksCopy)
}

private fun <T> ArrayDeque<T>.removeFirst(n: Int): List<T> = buildList {
    repeat(n) {
        add(this@removeFirst.removeFirst())
    }
}

private fun parseInput(input: String): Pair<List<ArrayDeque<Char>>, List<Step>> {
    val (cratesInput, stepsInput) = input.split("\n\n")

    val stacks = parseCratesInput(cratesInput)
    val steps = parseStepsInput(stepsInput)

    return stacks to steps
}

private fun parseCratesInput(cratesInput: String): List<ArrayDeque<Char>> {
    val crateRows: List<List<Char?>> = cratesInput
        .lines()
        .dropLast(1)
        .map { line ->
            line.chunked(4)
                .map { it.trim().getOrNull(1) }
        }

    return List(crateRows.first().size) { stackIndex ->
        ArrayDeque(crateRows.mapNotNull { it[stackIndex] })
    }
}

private fun parseStepsInput(stepsInput: String): List<Step> =
    stepsInput
        .lines()
        .map { line ->
            val stepWords = line.split(" ")

            Step(
                moves = stepWords[1].toInt(),
                originStack = stepWords[3].toInt(),
                destinationStack = stepWords[5].toInt()
            )
        }

private fun stacksToAnswer(stacks: Iterable<Iterable<Char>>) =
    stacks.map { it.first() }.joinToString("")

private data class Step(val moves: Int, val originStack: Int, val destinationStack: Int)
