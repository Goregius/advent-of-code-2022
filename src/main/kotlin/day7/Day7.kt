package day7

import utils.readInput

sealed interface FileSystemItemBuilder {
    val name: String
}

data class DirectoryInfo(
    override val name: String,
    val children: MutableSet<FileSystemItemBuilder> = mutableSetOf(),
    var size: Int = 0,
) : FileSystemItemBuilder
data class FileInfo(val size: Int, override val name: String) : FileSystemItemBuilder

private fun main() {
    val input = readInput(7)
    val lines = input.lines()
    val commandOutputs = parseToCommandOutputs(lines)

    println(part1(buildFileSystem(commandOutputs)))
    println(part2(buildFileSystem(commandOutputs)))
}

private fun part1(fileSystem: DirectoryInfo): Int {
    return fileSystem.traverseDirectories().sumOf { if (it.size <= 100000) it.size else 0 }
}

fun part2(fileSystem: DirectoryInfo, totalDiskSpace: Int = 70000000, desiredUnusedSpace: Int = 30000000): Int {
    val usedDiskSpace = fileSystem.size
    val unusedDiskSpace = totalDiskSpace - usedDiskSpace
    val minDiskSpaceToDelete = desiredUnusedSpace - unusedDiskSpace
    return fileSystem.traverseDirectories().filter { it.size >= minDiskSpaceToDelete }.minOf { it.size }
}

private fun buildFileSystem(commandOutputs: List<Pair<String, List<String>>>): DirectoryInfo {
    val directoryDeque = ArrayDeque(listOf(DirectoryInfo(name = "/")))

    for (commandOutput in commandOutputs) {
        processCommand(commandOutput, directoryDeque)
    }
    return directoryDeque.first()
}

private fun processCommand(commandOutput: Pair<String, List<String>>, directoryDeque: ArrayDeque<DirectoryInfo>) {
    val (command, outputs) = commandOutput
    when (command.substring(0..1)) {
        "cd" -> changeDirectoryTo(command.split(" ")[1], directoryDeque)
        "ls" -> directoryDeque.last().addChildren(outputs, directoryDeque)
    }
}

private fun changeDirectoryTo(path: String, directoryDeque: ArrayDeque<DirectoryInfo>) {
    when (path) {
        ".." -> directoryDeque.removeLast()
        "/" -> directoryDeque.dropLastWhile { directoryDeque.size > 1 }
        else -> {
            val selectedDirectory = directoryDeque.last().children
                .filterIsInstance<DirectoryInfo>().first { it.name == path }
            directoryDeque.add(selectedDirectory)
        }
    }
}

private fun DirectoryInfo.addChildren(outputs: List<String>, directoryDeque: ArrayDeque<DirectoryInfo>) {
    for (output in outputs) {
        val args = output.split(" ")
        if (args[0] == "dir") {
            this.children.add(DirectoryInfo(args[1]))
        } else {
            if (!this.children.any { it.name == args[1] }) {
                val size = args[0].toInt()
                this.children.add(FileInfo(size, args[1]))
                directoryDeque.addSize(size)
            }
        }
    }
}

private fun List<DirectoryInfo>.addSize(size: Int) {
    for (directoryInfo in this.reversed()) {
        directoryInfo.size += size
    }
}

private fun parseToCommandOutputs(lines: List<String>): List<Pair<String, List<String>>> {
    val commandOutputs = mutableListOf<Pair<String, MutableList<String>>>()
    for (line in lines) {
        if (line.startsWith("$")) {
            commandOutputs += line.drop(2) to mutableListOf()
        } else {
            commandOutputs.last().second += line
        }
    }
    return commandOutputs
}

fun DirectoryInfo.traverseDirectories(): Sequence<DirectoryInfo> = sequence {
    val queue = ArrayDeque<DirectoryInfo>()
    queue.addFirst(this@traverseDirectories)

    while(queue.isNotEmpty()) {
        val currentDirectory = queue.removeLast()

        yield(currentDirectory)

        for(childNode in currentDirectory.children.filterIsInstance<DirectoryInfo>()) {
            queue.addFirst(childNode)
        }
    }
}