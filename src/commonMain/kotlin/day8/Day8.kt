package day8

fun part1(input: String): Int {
    val treeGrid = parseInput(input)
    return treesWithTreesInAllDirections(treeGrid).count { (tree, treesInAllDirections) ->
        treesInAllDirections
            .map { trees -> trees.all { it < tree } }
            .any { it }
    }
}

fun part2(input: String): Int {
    val treeGrid = parseInput(input)
    return treesWithTreesInAllDirections(treeGrid).maxOf { (tree, treesInAllDirections) ->
        treesInAllDirections
            .map { countTreesUntilBlocked(it, tree) }
            .reduce { acc, treesUntilBlocked -> acc * treesUntilBlocked }
    }
}

private fun parseInput(input: String): List<List<Int>> {
    return input.lines().map { line -> line.map { it.digitToInt() } }
}

private fun countTreesUntilBlocked(trees: List<Int>, treeHeight: Int): Int {
    for (i in trees.indices) {
        if (trees[i] >= treeHeight) return i + 1
    }
    return trees.size
}

private fun treesWithTreesInAllDirections(treeGrid: List<List<Int>>): List<Pair<Int, List<List<Int>>>> =
    treeGrid.flatMapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, treeHeight ->
            treeHeight to treesInAllDirections(
                treeGrid = treeGrid,
                rowIndex = rowIndex,
                colIndex = colIndex
            )
        }
    }

private fun treesInAllDirections(treeGrid: List<List<Int>>, rowIndex: Int, colIndex: Int): List<List<Int>> {
    val row = treeGrid[rowIndex]
    val col = treeGrid.indices.map { treeGrid[it][colIndex] }
    return treesInBothDirections(row, colIndex) + treesInBothDirections(col, rowIndex)
}

private fun treesInBothDirections(treeLine: List<Int>, index: Int): List<List<Int>> =
    listOf(treeLine.subList(0, index).reversed(), treeLine.subList(index + 1, treeLine.size))