package day8

import utils.readInput

private fun main() {
    val input = readInput(8)
    val treeGrid = input.lines().map { line -> line.map { it.digitToInt() } }

    println(part1(treeGrid))
    println(part2(treeGrid))
}

private fun part1(treeGrid: List<List<Int>>): Int {
    return treeGrid.flatMapIndexed { rowIndex, row ->
        List(row.size) { colIndex ->
            isTreeVisibleInGrid(treeGrid = treeGrid, rowIndex = rowIndex, colIndex = colIndex)
        }
    }.count { it }
}

private fun part2(treeGrid: List<List<Int>>): Int {
    return treeGrid.flatMapIndexed { rowIndex, row ->
        List(row.size) { colIndex ->
            calculateScenicScore(treeGrid = treeGrid, rowIndex = rowIndex, colIndex = colIndex)
        }
    }.max()
}

private fun isTreeVisibleInGrid(treeGrid: List<List<Int>>, rowIndex: Int, colIndex: Int): Boolean {
    if (isTreeAnEdge(treeGrid, rowIndex, colIndex)) return true

    val row = treeGrid[rowIndex]
    val col = treeGrid.indices.map { treeGrid[it][colIndex] }

    return isTreeVisibleInLine(row, colIndex) || isTreeVisibleInLine(col, rowIndex)
}

private fun isTreeVisibleInLine(treeLine: List<Int>, index: Int): Boolean {
    val left = treeLine.subList(0, index)
    val tree = treeLine[index]
    val right = treeLine.subList(index + 1, treeLine.size)

    return left.all { it < tree } || right.all { it < tree }
}

private fun calculateScenicScore(treeGrid: List<List<Int>>, rowIndex: Int, colIndex: Int): Int {
    if (isTreeAnEdge(treeGrid, rowIndex, colIndex)) return 0

    val row = treeGrid[rowIndex]
    val col = treeGrid.indices.map { treeGrid[it][colIndex] }

    return calculateScenicScoreInLine(row, colIndex) * calculateScenicScoreInLine(col, rowIndex)
}

private fun calculateScenicScoreInLine(treeLine: List<Int>, index: Int): Int {
    val left = treeLine.subList(0, index).reversed()
    val tree = treeLine[index]
    val right = treeLine.subList(index + 1, treeLine.size)

    return countTreesUntilBlocked(left, tree) * countTreesUntilBlocked(right, tree)
}

private fun countTreesUntilBlocked(trees: List<Int>, treeHeight: Int): Int {
    for (i in trees.indices) {
        if (trees[i] >= treeHeight) return i + 1
    }
    return trees.size
}

private fun isTreeAnEdge(treeGrid: List<List<Int>>, rowIndex: Int, colIndex: Int): Boolean =
    rowIndex == 0 || rowIndex == treeGrid.size - 1 || colIndex == 0 || colIndex == treeGrid.size - 1