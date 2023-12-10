fun main() {
    fun part1(input: List<String>): Long {
        return input.map {
            val (winningNumbers, candidateNumbers) = it.dropWhile { c -> c != ':' }.removePrefix(": ").split("|")
            val winningNumberList = winningNumbers.split(" ").mapNotNull { w -> w.toIntOrNull() }
            val candidateNumberList = candidateNumbers.split(" ").mapNotNull { c -> c.toIntOrNull() }
            candidateNumberList.fold(0L) { acc, can ->
                if (winningNumberList.contains(can)) {
                    if (acc == 0L) {
                        return@fold acc + 1L
                    } else {
                        return@fold acc * 2L
                    }
                }
                    return@fold acc

            }
        }.sumOf { it }
    }

    fun part2(input: List<String>): Int {
        val cardMap = MutableList(input.size){1}
        input.forEachIndexed {index,card->
            val (winningNumbers, candidateNumbers) = card.dropWhile { c -> c != ':' }.removePrefix(": ").split("|")
            val winningNumberList = winningNumbers.split(" ").mapNotNull { w -> w.toIntOrNull() }
            val candidateNumberList = candidateNumbers.split(" ").mapNotNull { c -> c.toIntOrNull() }
            val numberOfWinners = candidateNumberList.fold(0) { acc, can ->
                if (winningNumberList.contains(can)) {
                    if (acc == 0) {
                        return@fold acc + 1
                    } else {
                        return@fold acc + 1
                    }
                }
                return@fold acc

            }
            for (i in (index+1)..(index+numberOfWinners)){
                cardMap[i] = cardMap[index]+cardMap[i]
            }
        }
        return cardMap.sumOf { it }
    }

    val testInput = readInput("Day04_test")
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))

}