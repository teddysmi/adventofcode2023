fun main() {
    val digitMap = mapOf("one" to 1, "two" to 2,
            "three" to 3, "four" to 4, "five" to 5,
            "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9,
            "1" to 1, "2" to 2, "3" to 3, "4" to 4, "5" to 5, "6" to 6, "7" to 7, "8" to 8, "9" to 9)

    fun part1(input: List<String>): Int {
        return input.map {
            "${it.first { c -> c.isDigit() }}${it.last { c -> c.isDigit() }}".toInt()
        }.sumOf { it }
    }

    fun part2(input: List<String>): Int {
        return input.mapIndexed { index, it ->
            var firstDigitIndex = Int.MAX_VALUE
            var firstChar = 0
            for (digit in digitMap.keys) {
                val currentIndex = it.indexOf(digit)
                if (-1 < currentIndex && currentIndex < firstDigitIndex) {
                    firstDigitIndex = currentIndex
                    firstChar = digitMap.get(digit) ?: 0
                }
            }
            var lastDigitIndex = -1
            var lastChar = 0
            for (digit in digitMap.keys) {
                val currentIndex = it.lastIndexOf(digit)
                if (currentIndex > lastDigitIndex) {
                    lastDigitIndex = currentIndex
                    lastChar = digitMap.get(digit) ?: 0
                }
            }
            firstChar * 10 + lastChar
        }.sumOf{it}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
