package day6

// https://adventofcode.com/2024/day/6

import java.io.File

class GuardGallivant(fileName: String) {
    private val labMap: MutableList<MutableList<Char>>
    private val directions = mapOf(
        '^' to Pair(-1, 0),
        '>' to Pair(0, 1),
        'v' to Pair(1, 0),
        '<' to Pair(0, -1),
    )
    private val turns = mapOf(
        '^' to '>',
        '>' to 'v',
        'v' to '<',
        '<' to '^',
    )

    init {
        labMap = openFile(fileName)
    }

    private fun openFile(fileName: String): MutableList<MutableList<Char>> {
        return File(fileName).readLines().map { it.toMutableList() }.toMutableList()
    }

    private fun findGuard(): Pair<Char, Pair<Int, Int>> {
        val guardChars = setOf('^', '>', 'v', '<')
        for (i in labMap.indices) {
            for (j in labMap[i].indices) {
                if (labMap[i][j] in guardChars) {
                    return Pair(labMap[i][j], i to j)
                }
            }
        }
        return Pair('X', -1 to -1)
    }

    private fun moveGuard(currentDirection: Char, currentPosition: Pair<Int, Int>): Pair<Char, Pair<Int, Int>> {
        val (dx, dy) = directions[currentDirection]!!
        val (cx, cy) = currentPosition
        val x = cx + dx
        val y = cy + dy
        if (x !in labMap.indices || y !in labMap[x].indices) {
            return Pair('X', -1 to -1)
        }
        if (labMap[x][y] == '#') {
            return Pair(turns[currentDirection]!!, currentPosition)
        }
        return Pair(currentDirection, x to y)
    }

    fun countGuardPath(): Int {
        val guard = findGuard()
        var currentDirection = guard.first
        var currentPosition = guard.second
        val visited = mutableSetOf<Pair<Int, Int>>()
        visited.add(currentPosition)
        while (currentDirection != 'X') {
            val (newDirection, newPosition) = moveGuard(currentDirection, currentPosition)
            currentDirection = newDirection
            currentPosition = newPosition
            if (currentDirection != 'X') {
                visited.add(currentPosition)
            }
        }
        return visited.size
    }

    fun countLoopOptions(): Int {
        val guard = findGuard()
        val startDirection = guard.first
        val startPosition = guard.second
        val loopOptions = mutableSetOf<Pair<Int, Int>>()
        for (i in labMap.indices) {
            for (j in labMap[i].indices) {
                if (labMap[i][j] == '.' && (i to j) != startPosition) {
                    labMap[i][j] = '#'
                    var currentDirection = startDirection
                    var currentPosition = startPosition
                    val visited = mutableSetOf<Pair<Char, Pair<Int, Int>>>()
                    var isLoop = false
                    while (currentDirection != 'X') {
                        val state = currentDirection to currentPosition
                        if (state in visited) {
                            isLoop = true
                            break
                        }
                        visited.add(state)
                        val (newDirection, newPosition) = moveGuard(currentDirection, currentPosition)
                        currentDirection = newDirection
                        currentPosition = newPosition
                    }
                    if (isLoop) {
                        loopOptions.add(i to j)
                    }
                    labMap[i][j] = '.'
                }
            }
        }
        return loopOptions.size
    }
}

fun main() {
    val input = "src/main/kotlin/day6/input.txt"
    val gg = GuardGallivant(input)
    val positionsVisited = gg.countGuardPath()
    println("Positions visited: $positionsVisited")
    val loopOptions = gg.countLoopOptions()
    println("Loop options: $loopOptions")
}
