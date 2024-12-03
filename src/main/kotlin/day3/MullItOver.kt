package day3

// https://adventofcode.com/2024/day/3

import java.io.File

class MullItOver {
    fun openFile(fileName: String): String {
        return File(fileName).readText()
    }

    fun useUglyRegex(str: String): Int {
        val regex = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")
        val findings = regex.findAll(str)
        val sums = findings.map { item ->
            val (x, y) = item.destructured
            x.toInt() * y.toInt()
        }
        return sums.sum()
    }

    fun useUglierRegex(str: String): Int {
        val mullRegex = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")
        val doRegex = Regex("""do\(\)""")
        val dontRegex = Regex("""don't\(\)""")
        val combinedRegex = Regex("""mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)""")

        var sum = 0
        var isSumEnabled = true
        val findings = combinedRegex.findAll(str)
        for (finding in findings) {
            val item = finding.value
            when {
                mullRegex.matches(item) -> {
                    if (isSumEnabled) {
                        val (x, y) = mullRegex.matchEntire(item)!!.destructured
                        sum += x.toInt() * y.toInt()
                    }
                }
                doRegex.matches(item) -> {
                    isSumEnabled = true
                }
                dontRegex.matches(item) -> {
                    isSumEnabled = false
                }
            }
        }
        return sum
    }
}

fun main() {
    val mio = MullItOver()
    val input = "src/main/kotlin/day3/input.txt"
    val text = mio.openFile(input)
    val sum = mio.useUglyRegex(text)
    println("Sum: $sum")
    val enabledSum = mio.useUglierRegex(text)
    println("Sum with do/dont: $enabledSum")
}