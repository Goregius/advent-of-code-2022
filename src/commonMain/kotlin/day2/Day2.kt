package day2

fun part1(input: String): Int {
    val rounds = input.lines().map { line ->
        line[0].toShape() to line[2].toShape()
    }

    return calculateScore(rounds)
}

fun part2(input: String): Int {
    val rounds = input.trim().lines().map { line ->
        val opponentShape = line[0].toShape()
        val result = line[2].toResult()
        val shape = opponentShape shapeForResult result

        opponentShape to shape
    }

    return calculateScore(rounds)
}

private fun calculateScore(rounds: Iterable<Pair<Shape, Shape>>) =
    rounds.sumOf { it.second.score + (it.second resultAgainst it.first).score }

private fun Char.toShape(): Shape = when (this) {
    'A', 'X' -> Shape.Rock
    'B', 'Y' -> Shape.Paper
    'C', 'Z' -> Shape.Scissors
    else -> error("invalid char '$this' for a shape")
}

private  fun Char.toResult(): Result = when (this) {
    'X' -> Result.Loss
    'Y' -> Result.Draw
    'Z' -> Result.Win
    else -> error("invalid char '$this' for a result")
}

private infix fun Shape.shapeForResult(result: Result) = when (this) {
    Shape.Rock -> when (result) {
        Result.Win -> Shape.Paper
        Result.Loss -> Shape.Scissors
        Result.Draw -> Shape.Rock
    }

    Shape.Paper -> when (result) {
        Result.Win -> Shape.Scissors
        Result.Loss -> Shape.Rock
        Result.Draw -> Shape.Paper
    }

    Shape.Scissors -> when (result) {
        Result.Win -> Shape.Rock
        Result.Loss -> Shape.Paper
        Result.Draw -> Shape.Scissors
    }
}

private infix fun Shape.resultAgainst(otherShape: Shape): Result = when (this) {
    Shape.Rock -> when (otherShape) {
        Shape.Rock -> Result.Draw
        Shape.Paper -> Result.Loss
        Shape.Scissors -> Result.Win
    }

    Shape.Paper -> when (otherShape) {
        Shape.Rock -> Result.Win
        Shape.Paper -> Result.Draw
        Shape.Scissors -> Result.Loss
    }

    Shape.Scissors -> when (otherShape) {
        Shape.Rock -> Result.Loss
        Shape.Paper -> Result.Win
        Shape.Scissors -> Result.Draw
    }
}

private enum class Shape(val score: Int) {
    Rock(1), Paper(2), Scissors(3)
}

private enum class Result(val score: Int) {
    Win(6), Loss(0), Draw(3)
}