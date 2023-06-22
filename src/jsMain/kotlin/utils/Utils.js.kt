package utils

import node.buffer.BufferEncoding
import node.process.process

actual fun readInput(day: Int): String {
    val path = process.env["AOC_2022_INPUT_DIR"] ?: error("The 'AOC_2022_INPUT_DIR' environment variable could not be found.")
    val inputPath = node.path.path.join(path, "input$day.txt")
    return node.fs.readFileSync(inputPath).toString(BufferEncoding.utf8).trimEnd()
}