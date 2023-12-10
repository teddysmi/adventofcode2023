fun main() {
    fun part1(input: List<String>): Long {
        return input.map{
            line->
            val stepList = mutableListOf(line.split(" ").mapNotNull { it.toLongOrNull() })
            while(stepList.last().any { it != 0L }){
                stepList.add(stepList.last().windowed(2,1).map { it[1]-it[0] })
            }
            stepList.foldRight(0L){
                next,acc->
                next.last()+acc
            }
        }.sumOf { it }
    }

    fun part2(input: List<String>): Long {
        return input.map{
            line->
            val stepList = mutableListOf(line.split(" ").mapNotNull { it.toLongOrNull() })
            while(stepList.last().any { it != 0L }){
                stepList.add(stepList.last().windowed(2,1).map { it[1]-it[0] })
            }
            stepList.foldRight(0L){
                next,acc->
                next.first()-acc
            }
        }.sumOf { it }
    }


    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114L)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))

}