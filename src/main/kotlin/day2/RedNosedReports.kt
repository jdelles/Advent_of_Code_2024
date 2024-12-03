package day2

import java.io.File

// https://adventofcode.com/2024/day/2

class RedNosedReports {
    fun openFile(fileName: String): List<List<Int>> {
        val lists = mutableListOf<List<Int>>()
        File(fileName).forEachLine { line ->
            val list = mutableListOf<Int>()
            val columns = line.split("\\s+".toRegex())
            for (item in columns) {
                val value = item.toIntOrNull()
                value?.let { list.add(value) }
            }
            lists.add(list)
        }
        return lists
    }

    private fun isSafeReport(list: List<Int>): Boolean {
        val increasing = list[1] > list[0]
        for (i in 0..< list.size - 1) {
            val diff = list[i + 1] - list[i]
            if (diff !in 1..3 && diff !in -3..-1) {
                return false
            }
            if (increasing != list[i + 1] > list[i]) {
                return false
            }
        }
        return true
    }

    private fun isSafeWithProblemDampener(list: List<Int>): Boolean {
        for (i in list.indices) {
            val dampenedList = list.filterIndexed { index, _ -> index != i }
            if (isSafeReport(dampenedList)) return true
        }
        return false
    }

    fun countSafeReports(lists: List<List<Int>>, hasProblemDampener: Boolean = false): Int {
        return lists.count { list ->
            isSafeReport(list) || if (hasProblemDampener) isSafeWithProblemDampener(list) else false
        }
    }

}

fun main() {
    val rnr = RedNosedReports()
    val input = "src/main/kotlin/Day2/input.txt"
    val lists = rnr.openFile(input)
    val safeReportsCount = rnr.countSafeReports(lists)
    println("Count of safe reports: $safeReportsCount")
    val safeReportsCountWithDampener = rnr.countSafeReports(lists, true)
    println("Count of safe reports with dampener: $safeReportsCountWithDampener")
}