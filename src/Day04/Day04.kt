package Day04

import println
import readInput
import java.util.*
import kotlin.math.pow

private const val PATH = "Day04/"

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { ScratchCard.parse(it) }.sumOf { it.points() }
    }

    fun part2(input: List<String>): Int {
        val scratchCards = input.map { ScratchCard.parse(it) }.toList()
        val pile = ScratchCardPile(scratchCards)
        pile.scratchAll()
        val maxSize = pile.maxSize()
        println("Max size: $maxSize")
        return maxSize
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_part1_test", path = PATH)
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04", path = PATH)
    check(part1(input) == 24542)
    part2(input).println()
}

class ScratchCardPile(private val startingCards: List<ScratchCard>) {
    private val queue: Queue<ScratchCard> = LinkedList()
    private var scratched: Int = 0

    fun maxSize(): Int = scratched

    fun scratchAll() {
        println("Scratching all cards ...")
        startingCards.forEach { scratchAll(it.index) }
    }

    private fun scratchAll(index: Int) {
        startingCards.find { it.index == index }.run {
            println("\n\nMain cards: $index")
            scratch(index, false)
        }
        queue.filter { it.index == index }.forEach {
            println("\nQueue cards: $index")
            scratch(it.index, removeFromQueue = true)
        }
    }

    private fun scratch(index: Int, removeFromQueue: Boolean) {
        scratched++
        val winnings = startingCards.find { it.index == index }?.countWinnings() ?: 0
        println("Scratching${if (removeFromQueue) "(Copy)" else ""} card $index with $winnings winnings")
        for (i in 1 until winnings + 1) {
            println("Adding to queue: ${index + i}")
            addToQueue(index + i)
        }
        if (removeFromQueue) {
            val element = startingCards.find { it.index == index }
            queue.remove(element = element)
            println("Removing from queue: ${element!!.index} \n\n")
        }
    }

    private fun addToQueue(index: Int) {
        queue.add(startingCards.find { it.index == index })
    }
}

class ScratchCard(
    val index: Int, private val winnings: List<Int>, private val scratch: List<Int>
) {
    fun points(): Int {
        return when (val wins = countWinnings()) {
            0 -> 0
            1 -> 1
            2 -> 2
            else -> 2.0.pow((wins - 1).toDouble()).toInt()
        }
    }

    fun countWinnings(): Int {
        return scratch.intersect(winnings.toSet()).count()
    }

    companion object {
        fun parse(input: String): ScratchCard {
            val split = input.split(":")
            val cardId = split[0].removePrefix("Card ").trimIndent().toInt()
            val scratch = split[1]
            val scratchNumbers = scratch.split("|")
            val winnings = scratchNumbers[0].trim().split("\\s+".toRegex()).map { it.toInt() }
            val numbers = scratchNumbers[1].trim().split("\\s+".toRegex()).map { it.toInt() }
            return ScratchCard(cardId, winnings, numbers)
        }
    }

}