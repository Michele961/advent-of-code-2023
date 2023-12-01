fun main() {
    fun part1(input: List<String>): Int {
        return input.map { Calibrator.calculateCalibrationValue(it) }
            .reduce(Int::plus)
    }

    fun part2(input: List<String>): Int {
        return input
            .map { Calibrator.calculateCalibrationValue(it) }
            .reduce(Int::plus)
    }

    val input = readInput("Day01")
    println("part1: " + part1(input))
    println("part2: " + part2(input))

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val testInput2 = readInput("Day01_part2_test")
    check(part2(testInput2) == 281)
}

object Calibrator {
    private const val EMPTY_STRING = ""
    private const val ZERO_CONSTANT = "0"
    private const val ALL_LETTER_PATTERN = "[A-Za-z]"
    private val toNumber = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    )

    fun calculateCalibrationValue(input: String): Int {
        val converted = convertTextToNumbers(input)
        return "${getFirstDigit(converted)}${getLastDigit(converted)}".toInt()
    }

    private fun convertTextToNumbers(input: String): String {
        var result = input
        toNumber.keys.forEach { key ->
            if (result.contains(key)) {
                result = result.replace(
                    key,
                    key.first() + toNumber[key].toString() + key.last(),
                    true
                )
            }
        }
        return cleanLetters(result)
    }

    private fun cleanLetters(word: String): String = word.replace(Regex(ALL_LETTER_PATTERN), EMPTY_STRING)

    private fun getFirstDigit(input: String): String = input.firstOrNull { it.isDigit() }?.toString() ?: ZERO_CONSTANT

    private fun getLastDigit(input: String): String = input.lastOrNull { it.isDigit() }?.toString() ?: ZERO_CONSTANT
}