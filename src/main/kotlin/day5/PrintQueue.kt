package day5

// https://adventofcode.com/2024/day/5

import java.io.File

class PrintQueue(fileName: String) {
    private var rules: List<Pair<Int, Int>>
    private var updates: List<List<Int>>

    init {
        val (parsedRules, parsedUpdates) = openFile(fileName)
        rules = parsedRules
        updates = parsedUpdates
    }

    private fun openFile(fileName: String): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
        val input = File(fileName).readText().split("\n\n")

        val rules = input[0].lines().map { line: String ->
            val (x, y) = line.split("|").map(String::toInt)
            x to y
        }

        val updates = input[1].lines().map { line: String ->
            line.split(",").map(String::toInt)
        }

        return rules to updates
    }

    private fun isValidUpdate(update: List<Int>): Boolean {
        val positions = update.withIndex().associate { it.value to it.index }
        return rules.all { (x, y) ->
            if (x in positions && y in positions) {
                positions[x]!! < positions[y]!!
            } else {
                true
            }
        }
    }

    private fun getMiddleValue(update: List<Int>) = update[update.size / 2]

    fun tallyValidUpdates(): Int {
        val validUpdates = updates.filter { isValidUpdate(it) }
        return validUpdates.sumOf { getMiddleValue(it) }
    }

    /**
     * ChatGPT help after my own attempt to create all permutations
     * obviously timed out in O(n!!!!!!!).
     *
     * This is a graph approach that is O(V + E) time instead.
     */

    private fun getValidOrder(update: List<Int>): List<Int>? {
        val inDegree = mutableMapOf<Int, Int>()
        val graph = mutableMapOf<Int, MutableList<Int>>()

        update.forEach { page ->
            inDegree[page] = 0
            graph[page] = mutableListOf()
        }

        rules.forEach { (x, y) ->
            if (x in update && y in update) {
                graph[x]?.add(y)
                inDegree[y] = inDegree.getOrDefault(y, 0) + 1
            }
        }

        // Perform topological sort (Kahn's Algorithm)
        val queue = ArrayDeque<Int>()
        inDegree.filter { it.value == 0 }.keys.forEach { queue.add(it) }
        val sortedOrder = mutableListOf<Int>()

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            sortedOrder.add(current)

            graph[current]?.forEach { neighbor ->
                inDegree[neighbor] = inDegree[neighbor]!! - 1
                if (inDegree[neighbor] == 0) {
                    queue.add(neighbor)
                }
            }
        }

        return if (sortedOrder.size == update.size) sortedOrder else null
    }

    private fun getCorrectedUpdates(invalidUpdates: List<List<Int>>): List<List<Int>> {
        val correctedUpdates = mutableListOf<List<Int>>()
        invalidUpdates.forEach { update ->
            val validOrder = getValidOrder(update)
            if (validOrder != null) {
                correctedUpdates.add(validOrder)
            }
        }
        return correctedUpdates
    }

    fun correctAndTallyInvalidUpdates(): Int {
        val invalidUpdates = updates.filter { !isValidUpdate(it) }
        val correctedUpdates = getCorrectedUpdates(invalidUpdates)
        return correctedUpdates.sumOf { getMiddleValue(it) }
    }
}

fun main() {
    val input = "src/main/kotlin/day5/input.txt"
    val pq = PrintQueue(input)
    val tallyValidUpdates = pq.tallyValidUpdates()
    println("Valid updates: $tallyValidUpdates")
    val correctAndTallyInvalidUpdates = pq.correctAndTallyInvalidUpdates()
    println("corrected invalid updates: $correctAndTallyInvalidUpdates")
}