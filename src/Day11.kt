import kotlin.math.abs

fun main() {
    data class Vec2(val x: Long, val y: Long)

    fun part1(input: List<String>): Long {
        val galaxyPositions = input.foldIndexed(mutableListOf<Vec2>()) { row, acc, next ->
            next.foldIndexed(acc) { col, innerAcc, c ->
                if (c == '#') {
                    innerAcc.add(Vec2(row.toLong(), col.toLong()))
                }
                innerAcc
            }
        }
        val emptyRows = (0..input.lastIndex).fold(listOf<Int>()) { acc, row ->
            val galaxiesInRow = galaxyPositions.filter { it.x == row.toLong() }
            if (galaxiesInRow.isEmpty()) {
                acc + row
            } else {
                acc
            }
        }
        val emptyColumns = (0..input[0].lastIndex).fold(listOf<Int>()) { acc, col ->
            val galaxiesInCol = galaxyPositions.filter { it.y == col.toLong() }
            if (galaxiesInCol.isEmpty()) {
                acc + col
            } else {
                acc
            }
        }
        //update galaxy positions

        emptyRows.forEachIndexed { index, i ->
            for (pos in 0..galaxyPositions.lastIndex) {
                val galaxy = galaxyPositions[pos]
                if (galaxy.x > i + index) {
                    galaxyPositions[pos] = Vec2(galaxy.x + 1, galaxy.y)
                }
            }
        }
        emptyColumns.forEachIndexed { index, j ->
            for (pos in 0..galaxyPositions.lastIndex) {
                val galaxy = galaxyPositions[pos]
                if (galaxy.y > j + index) {
                    galaxyPositions[pos] = Vec2(galaxy.x, galaxy.y + 1)
                }
            }
        }
        var distance = 0L
        for (i in 0..galaxyPositions.lastIndex) {
            for (j in i + 1..galaxyPositions.lastIndex) {
                val p1 = galaxyPositions[i]
                val p2 = galaxyPositions[j]
                distance += abs(p2.x - p1.x) + abs(p2.y - p1.y)
            }
        }
        return distance
    }

    fun part2(input: List<String>): Long {
        val galaxyPositions = input.foldIndexed(mutableListOf<Vec2>()) { row, acc, next ->
            next.foldIndexed(acc) { col, innerAcc, c ->
                if (c == '#') {
                    innerAcc.add(Vec2(row.toLong(), col.toLong()))
                }
                innerAcc
            }
        }
        val emptyRows = (0..input.lastIndex).fold(listOf<Int>()) { acc, row ->
            val galaxiesInRow = galaxyPositions.filter { it.x == row.toLong() }
            if (galaxiesInRow.isEmpty()) {
                acc + row
            } else {
                acc
            }
        }
        val emptyColumns = (0..input[0].lastIndex).fold(listOf<Int>()) { acc, col ->
            val galaxiesInCol = galaxyPositions.filter { it.y == col.toLong() }
            if (galaxiesInCol.isEmpty()) {
                acc + col
            } else {
                acc
            }
        }
        //update galaxy positions

        emptyRows.forEachIndexed { index, i ->
            for (pos in 0..galaxyPositions.lastIndex) {
                val galaxy = galaxyPositions[pos]
                if (galaxy.x > i + (index*999999L)) {
                    galaxyPositions[pos] = Vec2(galaxy.x + 999999L, galaxy.y)
                }
            }
        }
        emptyColumns.forEachIndexed { index, j ->
            for (pos in 0..galaxyPositions.lastIndex) {
                val galaxy = galaxyPositions[pos]
                if (galaxy.y > j + (index*999999L)) {
                    galaxyPositions[pos] = Vec2(galaxy.x, galaxy.y + 999999L)
                }
            }
        }
        var distance = 0L
        for (i in 0..galaxyPositions.lastIndex) {
            for (j in i + 1..galaxyPositions.lastIndex) {
                val p1 = galaxyPositions[i]
                val p2 = galaxyPositions[j]
                distance += abs(p2.x - p1.x) + abs(p2.y - p1.y)
            }
        }
        return distance
    }


    val testInput = readInput("Day11_test")
//    check(part2(testInput) == 8410L)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))

}