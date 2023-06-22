package day9

fun part1(input: String): Int = calculateVisitedPositions(parseInput(input), 2)
fun part2(input: String): Int = calculateVisitedPositions(parseInput(input), 10)

private fun parseInput(input: String): List<Vector2D> = input.lines().flatMap { line ->
    line.split(" ").let { (direction, numSteps) ->
        List(numSteps.toInt()) { directionToVector.getValue(direction[0]) }
    }
}

private fun calculateVisitedPositions(steps: Iterable<Vector2D>, knotsCount: Int): Int {
    val positions = MutableList(knotsCount) { Vector2D.Zero }
    val visitedPositions = mutableSetOf<Vector2D>()

    for (step in steps) {
        positions[0] += step

        for (i in 1 until knotsCount) {
            positions[i] = calculateNewTailPosition(positions[i], positions[i - 1])
        }
        visitedPositions.add(positions[knotsCount - 1])
    }

    return visitedPositions.size
}

private fun calculateNewTailPosition(tailPosition: Vector2D, newHeadPosition: Vector2D): Vector2D {
    val (dx, dy) = newHeadPosition - tailPosition
    if (dx in -1..1 && dy in -1..1) return tailPosition
    return tailPosition + Vector2D(dx.coerceIn(-1..1), dy.coerceIn(-1..1))
}

private val directionToVector = mapOf(
    'U' to Vector2D(0, 1),
    'D' to Vector2D(0, -1),
    'L' to Vector2D(-1, 0),
    'R' to Vector2D(1, 0)
)

private data class Vector2D(val x: Int, val y: Int) {
    operator fun plus(other: Vector2D) = Vector2D(x + other.x, y + other.y)
    operator fun minus(other: Vector2D) = Vector2D(x - other.x, y - other.y)

    companion object {
        val Zero = Vector2D(0, 0)
    }
}