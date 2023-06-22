package utils

private val obj = object {}

actual fun readInput(day: Int) = obj.javaClass.getResource("/input$day.txt").readText().trimEnd()