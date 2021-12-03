fun main() {
    fun part1(input: List<String>): Int {
        return input.windowed(2).count{(a,b)->a.toInt()<b.toInt()}
    }

    fun part2(input: List<String>): Int {
        return input.windowed(4).count{it[0].toInt()<it[3].toInt() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
