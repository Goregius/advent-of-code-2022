package day7

private sealed interface FileSystemItemBuilder {
    val name: String
}

private data class DirectoryInfo(
    override val name: String,
    val children: MutableSet<FileSystemItemBuilder> = mutableSetOf(),
    var size: Int = 0,
) : FileSystemItemBuilder
private data class FileInfo(val size: Int, override val name: String) : FileSystemItemBuilder

fun part1(input: String): Int {
    val fileSystem = parseInput(input)
    return fileSystem.traverseDirectories().sumOf { if (it.size <= 100000) it.size else 0 }
}

fun part2(input: String): Int {
    val fileSystem = parseInput(input)
    val usedDiskSpace = fileSystem.size
    val unusedDiskSpace = 70_000_000 - usedDiskSpace
    val minDiskSpaceToDelete = 30_000_000 - unusedDiskSpace
    return fileSystem.traverseDirectories().filter { it.size >= minDiskSpaceToDelete }.minOf { it.size }
}

private fun parseInput(input: String) = buildFileSystem(parseToCommandOutputs(input.lines()))

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

private fun DirectoryInfo.traverseDirectories(): Sequence<DirectoryInfo> = sequence {
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