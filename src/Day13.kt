import kotlin.math.min

fun main() {


    fun part1(input: List<String>): Long {
        val patternList = input.fold(mutableListOf(mutableListOf<String>())) { acc, next ->
            if (next.isBlank()) {
                acc.add(mutableListOf())
            } else {
                acc.last().add(next)
            }
            acc
        }
        val sum = patternList.fold(0L) { acc, pattern ->
            var sum = acc
            for (rowsAbove in 1 .. pattern.lastIndex) {
                var horizontalMatch = true
                val rowsBelow = pattern.size - rowsAbove
                for (i in 1..min(rowsBelow, rowsAbove)) {
                    if (pattern[rowsAbove - i] != pattern[rowsAbove + i - 1]) {
                        horizontalMatch = false
                        break
                    }
                }
                if (horizontalMatch) {
                    sum += rowsAbove * 100
                }
            }

            for (columnsLeft in 1 .. pattern[0].lastIndex) {
                var verticalMatch = true
                val columnsRight = pattern[0].length - columnsLeft
                for (string in pattern) {
                    val numberOfColumnsToCompare = min(columnsLeft, columnsRight)
                    if (string.substring(columnsLeft - numberOfColumnsToCompare, columnsLeft) != string.substring(
                            columnsLeft,
                            columnsLeft + numberOfColumnsToCompare
                        ).reversed()
                    ) {
                        verticalMatch = false
                        break
                    }
                }
                if (verticalMatch) {
                    sum += columnsLeft
                }
            }
            sum
        }
        return sum
    }

    fun part2(input: List<String>): Long {

        val patternList = input.fold(mutableListOf(mutableListOf<String>())) { acc, next ->
            if (next.isBlank()) {
                acc.add(mutableListOf())
            } else {
                acc.last().add(next)
            }
            acc
        }
        val sum = patternList.fold(0L) { acc, pattern ->
            var sum = acc
            for (rowsAbove in 1 .. pattern.lastIndex) {
                var numberOfDifference = 0
                val rowsBelow = pattern.size - rowsAbove
                for (i in 1..min(rowsBelow, rowsAbove)) {
                    val topString = pattern[rowsAbove - i]
                    val bottomString = pattern[rowsAbove + i - 1]
                    for ( j in topString.indices){
                        if (topString[j] != bottomString[j]){
                            numberOfDifference++
                        }
                        if (numberOfDifference > 1){
                            break
                        }
                    }
                }
                if (numberOfDifference == 1){
                    sum += rowsAbove*100
                }
            }

            for (columnsLeft in 1 .. pattern[0].lastIndex) {
                var numberOfDifference = 0
                val columnsRight = pattern[0].length - columnsLeft
                for (string in pattern) {
                    val numberOfColumnsToCompare = min(columnsLeft, columnsRight)
                    val leftString = string.substring(columnsLeft - numberOfColumnsToCompare, columnsLeft)
                    val rightString = string.substring(
                            columnsLeft,
                            columnsLeft + numberOfColumnsToCompare
                        ).reversed()
                    for (j in leftString.indices){
                        if (leftString[j] != rightString[j]){
                            numberOfDifference++
                        }
                        if (numberOfDifference > 1){
                            break
                        }
                    }

                }
                if (numberOfDifference == 1) {
                    sum += columnsLeft
                }
            }
            sum
        }
        return sum
    }


    val testInput = readInput("Day13_test")
    check(part2(testInput) == 400L)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))

}