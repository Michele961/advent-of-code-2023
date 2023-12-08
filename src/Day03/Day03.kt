package Day03

import println
import readInput

private const val PATH = "Day03/"

fun main() {
    fun part1(input: List<String>): Int {
        val engine = Engine(input)
        val numbers = engine.findNumbers()
        val filter = numbers.filter { it.isValid() }
        return filter.sumOf { it.value }
    }

    fun part2(input: List<String>): Int {
        return 2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_part1_test", path = PATH)
    val input = readInput("Day03", path = PATH)

    part1(input).println()

    part1(testInput).println()
    check(part1(testInput) == 4361)
//    check(part2(testInput) == -1)

}

class Number(
    val value: Int,
    private val start: Int,
    private val end: Int,
    val rowNum: Int,
    private val topRow: String?,
    private val bottomRow: String?,
    private val leftChar: Char? = null,
    private val rightChar: Char? = null,
    private val hisRow: String
) {
    private var leftValid: Boolean = false
    private var rightValid: Boolean = false
    private var topValid: Boolean = false
    private var bottomValid: Boolean = false
    private var isValid: Boolean

    init {
        isValid = checkValidity()
    }

    fun isValid(): Boolean = leftValid || rightValid || topValid || bottomValid

    private fun checkValidity(): Boolean {
        leftValid = leftChar?.let { it != '.' && !it.isDigit() } ?: false
        rightValid = rightChar?.let { it != '.' && !it.isDigit() } ?: false
        val leftLimit = if (start > 0) start - 1 else 0
        val rightLimit = if (end < hisRow.length - 1) end + 1 else end
        topValid =
            topRow?.let { topChar -> topChar.substring(leftLimit, rightLimit + 1).any { it != '.' && !it.isDigit() } }
                ?: false
        bottomValid = bottomRow?.let { bottomChar ->
            bottomChar.substring(leftLimit, rightLimit + 1)
                .any { it != '.' && !it.isDigit() }
        } ?: false
        return leftValid || rightValid || topValid || bottomValid
    }
}

class Engine(private val encoded: List<String>) {

    fun findNumbers(): MutableList<Number> {
        val regex = Regex("""\d+""")
        val result = mutableListOf<Number>()
        encoded.forEachIndexed { index, line ->
            val foundedNumbers = regex
                .findAll(line)
                .map {
                    Number(
                        value = it.value.toInt(),
                        start = it.range.first,
                        end = it.range.last,
                        rowNum = index,
                        topRow = if (index == 0) null else encoded[index - 1],
                        bottomRow = if (index == encoded.size - 1) null else encoded[index + 1],
                        leftChar = if (it.range.first == 0) null else encoded[index][it.range.first - 1],
                        rightChar = if (it.range.last == encoded[index].length - 1) null else encoded[index][it.range.last + 1],
                        hisRow = encoded[index]
                    )
                }
                .toList()
            result.addAll(foundedNumbers)
        }
        return result
    }
}
