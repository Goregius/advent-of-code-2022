package day3

fun part1(input: String): Int = input.lines().sumOf { bag ->
    val firstCompartment = bag.substring(0, bag.length / 2)
    val secondCompartment = bag.substring(bag.length / 2).toSet()

    firstCompartment.first { it in secondCompartment }.toPriority()
}

fun part2(input: String) = input.lines().chunked(3).sumOf { group ->
    val firstBag = group[0]
    val secondBag = group[1].toSet()
    val thirdBag = group[2].toSet()

    firstBag.first { it in secondBag && it in thirdBag }.toPriority()
}

private fun Char.toPriority(): Int {
    if (this in 'a'..'z') {
        return this.code - 96 // 'a' = 97
    }

    if (this in 'A'..'Z') {
        return this.code - 64 + 26 // 'A' = 65
    }

    throw IllegalArgumentException(
        "To be converted to a priority, the character should be a to z or A to Z, instead it was '$this'."
    )
}
