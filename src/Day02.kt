fun main() {
    fun part1(input: List<String>): Int {
        return input.map {
            var gameNumber = it.dropWhile { c -> !c.isDigit() }.takeWhile { c -> c != ':' }.trim().toInt()
            val game = it.dropWhile { c -> c != ':' }.removePrefix(": ").replace(",", "").replace(";", "").split(" ")
            for (pick in game.windowed(2, 2)) {
                if (pick[1] == "red" && pick[0].toInt() > 12) {
                    gameNumber = 0
                    break
                }
                if (pick[1] == "green" && pick[0].toInt() > 13) {
                    gameNumber = 0
                    break
                }
                if (pick[1] == "blue" && pick[0].toInt() > 14) {
                    gameNumber = 0
                    break
                }
            }
            gameNumber
        }.sumOf { it }
    }

    fun part2(input: List<String>): Int {

        return input.map {
            var maxRed = 0
            var maxGreen = 0
            var maxBlue = 0
            val game = it.dropWhile { c -> c != ':' }.removePrefix(": ").replace(",", "").replace(";", "").split(" ")
            for (pick in game.windowed(2, 2)) {
                if (pick[1] == "red" && pick[0].toInt() > maxRed) {
                    maxRed = pick[0].toInt()

                }
                if (pick[1] == "green" && pick[0].toInt() > maxGreen) {
                    maxGreen = pick[0].toInt()

                }
                if (pick[1] == "blue" && pick[0].toInt() > maxBlue) {
                    maxBlue = pick[0].toInt()

                }
            }
            maxRed * maxGreen * maxBlue
        }.sumOf { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

