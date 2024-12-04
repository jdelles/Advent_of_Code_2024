package day4

// https://adventofcode.com/2024/day/4

import java.io.File

class CeresSearch(fileName: String) {
    private var puzzle: List<String>
    private var rows: Int
    private var columns: Int
    private val directions = listOf(
        Pair(-1, -1),
        Pair(-1, 0),
        Pair(-1, 1),
        Pair(0, -1),
        Pair(0, 1),
        Pair(1, -1),
        Pair(1, 0),
        Pair(1, 1),
    )

    init {
        puzzle = openFile(fileName)
        rows = puzzle.size
        columns = puzzle[0].length
    }

    private fun openFile(fileName: String): List<String> {
        return File(fileName).readLines()
    }

    private fun searchWords(row: Int, col: Int, x: Int, y: Int): Boolean {
        val word = "XMAS"
        for (i in word.indices) {
            val r = row + i * x
            val c = col + i * y
            if (r !in 0..< rows || c !in 0..< columns || puzzle[r][c] != word[i]) {
                return false
            }
        }
        return true
    }

    fun countWords(): Int {
        var count = 0
        for (r in 0 ..< rows) {
            for (c in 0..< columns) {
                for ((x, y) in directions) {
                    if (searchWords(r, c, x, y)) {
                        count += 1
                    }
                }
            }
        }
        return count
    }

    private fun searchXWords(row: Int, col: Int): Boolean {
        if (puzzle[row][col] != 'A') return false
        val topLeft = Pair(row - 1, col - 1)
        val topRight = Pair(row - 1, col + 1)
        val bottomLeft = Pair(row + 1, col - 1)
        val bottomRight = Pair(row + 1, col + 1)
        if (
            topLeft.first !in 0..< rows ||
            topLeft.second !in 0..< columns ||
            topRight.first !in 0..< rows ||
            topRight.second !in 0..< columns ||
            bottomLeft.first !in 0..< rows ||
            bottomLeft.second !in 0..< columns ||
            bottomRight.first !in 0..< rows ||
            bottomRight.second !in 0..< columns
        ) {
            return false
        }
        val d1 = listOf(
            puzzle[topLeft.first][topLeft.second],
            puzzle[bottomRight.first][bottomRight.second]
        )
        val d2 = listOf(
            puzzle[topRight.first][topRight.second],
            puzzle[bottomLeft.first][bottomLeft.second]
        )
        val isValidDiagonal1 = d1 == listOf('M', 'S') || d1 == listOf('S', 'M')
        val isValidDiagonal2 = d2 == listOf('M', 'S') || d2 == listOf('S', 'M')
        return isValidDiagonal1 && isValidDiagonal2
    }

    fun countXWords(): Int {
        var count = 0
        for (r in 1..< rows) {
            for (c in 1..< columns) {
                if (searchXWords(r, c)) {
                    count++
                }
            }
        }
        return count
    }
}

fun main() {
    val input = "src/main/kotlin/day4/input.txt"
    val cs = CeresSearch(input)
    val count = cs.countWords()
    println("Total XMAS found: $count")
    val countX = cs.countXWords()
    println("Total X-MAS found: $countX")
}
