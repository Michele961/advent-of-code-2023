package Day02

import println
import readInput

private const val PATH = "Day02/"

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { game ->
            val gamesConverted = CubeGame.convert(game)
            if (gamesConverted.allGamesPossible()) {
                gamesConverted.id
            } else 0
        }.reduce(Int::plus)
    }

    fun part2(input: List<String>): Int {
        return input.map { game ->
            val gamesConverted = CubeGame.convert(game)
            gamesConverted.calculatePower()
        }.reduce(Int::plus)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_part1_test", path = PATH)
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02", path = PATH)
    part1(input).println()
    part2(input).println()
}

class CubeConfig(
    val blue: Int,
    val green: Int,
    val red: Int
)

data class CubeGame(
    val id: Int,
    val games: List<CubeSubGame>
) {

    fun calculatePower(): Int {
        val blueMax = games.maxBy { game ->
            game.blue
        }.blue.takeIf { it > 0 } ?: 1

        val greenMax = games.maxBy { game ->
            game.green
        }.green.takeIf { it > 0 } ?: 1
        val redMax = games.maxBy { game ->
            game.red
        }.red.takeIf { it > 0 } ?: 1

        return blueMax * greenMax * redMax
    }


    fun allGamesPossible(config: CubeConfig = CubeConfig(blue = 14, green = 13, red = 12)): Boolean {
        return games.all { game ->
            game.blue <= config.blue && game.green <= config.green && game.red <= config.red
        }
    }

    companion object {
        fun convert(game: String): CubeGame {
            val description = game
                .split(":")
            val gameId = description[0].removePrefix("Game ").toInt()
            val gamesPlayed = description[1].split(";")
            val subGames = gamesPlayed.map { g ->
                val trimmed = g.trimIndent()
                var blue = 0
                var green = 0
                var red = 0

                if (Regex("\\d+ blue").containsMatchIn(trimmed))
                    blue =
                        Regex("\\d+ blue").find(trimmed)!!.value.toColorValue()

                if (Regex("\\d+ green").containsMatchIn(trimmed))
                    green =
                        Regex("\\d+ green").find(trimmed)!!.value.toColorValue()

                if (Regex("\\d+ red").containsMatchIn(trimmed))
                    red =
                        Regex("\\d+ red").find(trimmed)!!.value.toColorValue()

                CubeSubGame(blue, green, red)
            }

            return CubeGame(gameId, subGames)
        }
    }
}

private fun String.toColorValue(): Int {
    return Regex("\\d+").find(this)!!.value.toInt()
}

data class CubeSubGame(
    val blue: Int,
    val green: Int,
    val red: Int
)