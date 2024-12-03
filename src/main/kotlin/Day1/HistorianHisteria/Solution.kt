package Day1.HistorianHisteria

import java.io.File
import kotlin.math.abs

class Solution {
    private fun openFile(fileName: String): Pair<List<Int>, List<Int>> {
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

    fun processFile(fileName: String): Int {
        val (list1, list2) = openFile(fileName)
        val list1Sorted = list1.sorted()
        val list2Sorted = list2.sorted()
        val zipped = list1Sorted.zip(list2Sorted)
        val diff = zipped.map { (a, b) -> abs(b - a)}
        return diff.sum()
    }
}

fun main() {
    val solution = Solution()
    val fileName = "src/main/kotlin/Day1/HistorianHisteria/input.txt"
    val result = solution.processFile(fileName)
    println("Result: $result")
}