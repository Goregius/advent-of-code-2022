package day8

import utils.readInput

private fun main() {
    val input = readInput(8)
    val treeGrid = input.lines().map { line -> line.map { it.digitToInt() } }

    println(part1(treeGrid))
    println(part2(treeGrid))
}

private fun part1(treeGrid: List<List<Int>>): Int =
    treesWithTreesInAllDirections(treeGrid).count { (tree, treesInAllDirections) ->
        treesInAllDirections
            .map { trees -> trees.all { it < tree } }
            .any { it }
    }

private fun part2(treeGrid: List<List<Int>>): Int =
    treesWithTreesInAllDirections(treeGrid).maxOf { (tree, treesInAllDirections) ->
        treesInAllDirections
            .map { countTreesUntilBlocked(it, tree) }
            .reduce { acc, treesUntilBlocked -> acc * treesUntilBlocked }
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