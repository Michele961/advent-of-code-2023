package Day04

import readInput
import kotlin.math.pow

private const val PATH = "Day04/"

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { ScratchCard.parse(it) }.sumOf { it.points() }
    }

    fun part2(input: List<String>): Int {
        val scratchCards = input.map { ScratchCard.parse(it) }.toList()
        val pile = ScratchCardQueue(scratchCards)
        pile.scratchAll()
        return pile.getScratchedCount()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_part1_test", path = PATH)
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04", path = PATH)
    check(part1(input) == 24542)
    check(part2(input) == 8736438)
}

class ScratchCardQueue(private val startingCards: List<ScratchCard>) {
    private var indexCopy: MutableMap<Int, Int> = mutableMapOf()
    private var scratched: Int = 0

    fun getScratchedCount(): Int = scratched

    fun scratchAll(): Int {
        startingCards.forEach { scratchAll(it.index) }
        val scratch = scratched
        indexCopy = mutableMapOf()
        return scratch
    }

    private fun scratchAll(index: Int) {
        startingCards.find { it.index == index }.run {
            scratch(index, makeCopy = indexCopy.getOrDefault(index, 0))
        }
    }

    private fun scratch(index: Int, makeCopy: Int = 0) {
        val winnings = startingCards.find { it.index == index }?.countWinnings() ?: 0
        val amountToIncrease = if (makeCopy != 0) makeCopy + 1 else 1
        winScratch(winnings, index, amountToIncrease)
        scratched += amountToIncrease
    }

    private fun winScratch(winnings: Int, index: Int, amountToIncrease: Int) {
        for (i in 1 until winnings + 1) {
            if (indexCopy[index + i] != null) {
                indexCopy.computeIfPresent(index + i) { _, v -> v + amountToIncrease }
            } else {
                indexCopy.computeIfAbsent(index + i) { amountToIncrease }
            }
        }
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