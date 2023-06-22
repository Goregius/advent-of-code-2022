package day1

fun part1(input: String): Int {
    val foodPerElf = parseInput(input)

    val caloriesPerElf = foodPerElf.calculateCaloriesPerElf()

    return caloriesPerElf.max()
}

fun part2(input: String): Int {
    val foodPerElf = parseInput(input)

    val caloriesPerElf = foodPerElf.calculateCaloriesPerElf()

    return caloriesPerElf.sortedDescending().take(3).sum()
}

private fun parseInput(input: String) = input
    .split("\n\n")
    .map { elfText ->
        elfText.trim().lines().map { it.toInt() }
    }

private fun List<List<Int>>.calculateCaloriesPerElf() = map { it.sum() }