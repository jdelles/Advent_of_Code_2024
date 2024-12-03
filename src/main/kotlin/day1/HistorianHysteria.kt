package day1

import java.io.File
import kotlin.math.abs

class HistorianHysteria {
    fun openFile(fileName: String): Pair<List<Int>, List<Int>> {
        val leftColumn = mutableListOf<Int>()
        val rightColumn = mutableListOf<Int>()
        File(fileName).forEachLine { line ->
            val columns = line.split("\\s+".toRegex())
            val left = columns[0].toIntOrNull()
            val right = columns[1].toIntOrNull()
            if (left != null && right != null) {
                leftColumn.add(left)
                rightColumn.add(right)
            }
        }
        return Pair(leftColumn, rightColumn)
    }

    fun getDifference(lists: Pair<List<Int>, List<Int>>): Int {
        val (left, right) = lists
        val leftSorted = left.sorted()
        val rightSorted = right.sorted()
        val zipped = leftSorted.zip(rightSorted)
        val diff = zipped.map { (a, b) -> abs(b - a)}
        return diff.sum()
    }

    fun getSimilarityScore(lists: Pair<List<Int>, List<Int>>): Int {
        val (left, right) = lists
        val rightCount = right.groupingBy { it }.eachCount()
        var similarityScore = 0
        for (item in left) {
            val count = rightCount[item] ?: 0
            similarityScore += item * count
        }
        return similarityScore
    }
}

fun main() {
    val hh = HistorianHysteria()
    val fileName = "src/main/kotlin/Day1/input.txt"
    val lists = hh.openFile(fileName)
    val difference = hh.getDifference(lists)
    println("Difference: $difference")
    val similarityScore = hh.getSimilarityScore(lists)
    println("Similarity Score: $similarityScore")
}