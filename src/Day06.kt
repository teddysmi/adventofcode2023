fun main() {
    fun part1(input: List<String>): Long {
        val timeDistancePair = input[0].substringAfter(":")
                .split(" ").mapNotNull { it.toLongOrNull() }
                .zip(input[1].substringAfter(":").split(" ").mapNotNull { it.toLongOrNull() })
        val product = timeDistancePair.fold(1L) { acc, next ->
            var speed = 0
            while (speed * (next.first - speed) <= next.second) {
                speed++
            }
            acc * (next.first - (2 * speed) + 1)
        }
        return product
    }

    fun part2(input: List<String>): Long {
        val timeDistancePair = Pair(input[0].substringAfter(":")
                .split(" ").mapNotNull { it.toLongOrNull() }.joinToString("") { it.toString() }.toLong(),
                input[1].substringAfter(":").split(" ").mapNotNull { it.toLongOrNull() }.joinToString("") { it.toString() }.toLong())
        var speed = 0
        while (speed * (timeDistancePair.first - speed) <= timeDistancePair.second) {
            speed++
        }
        return timeDistancePair.first - (2 * speed) + 1
    }


    val testInput = readInput("Day06_test")
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))

}