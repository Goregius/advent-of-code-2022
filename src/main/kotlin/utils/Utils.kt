package utils

private val obj = object {}
fun readInput(day: Int) = obj.javaClass.getResource("/input$day.txt").readText().trim()