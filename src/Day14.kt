fun main() {

    data class Vec2(val row: Int, val col: Int)

    fun part1(input: List<String>): Long {
        val worldMap = input.foldIndexed(mutableMapOf<Vec2, Char>()) { row, acc, next ->
            next.foldIndexed(acc) { col, innerAcc, c ->
                innerAcc[Vec2(row, col)] = c
                innerAcc
            }
            acc
        }
        var sum = 0L
        for (row in 0..input.lastIndex) {
            for (col in 0..input[0].lastIndex) {
                if (worldMap[Vec2(row, col)] == 'O') {
                    if (row == 0) {
                        sum += input.size - row
                    } else {
                        var currentRow = row
                        var charAbove = worldMap[Vec2(row - 1, col)] ?: '.'
                        while (charAbove == '.') {
                            worldMap[Vec2(currentRow - 1, col)] = 'O'
                            worldMap[Vec2(currentRow, col)] = '.'
                            currentRow--
                            if (currentRow == 0) {
                                break
                            }
                            charAbove = worldMap[Vec2(currentRow - 1, col)] ?: '.'
                        }
                        sum += input.size - currentRow
                    }

                }
            }
        }


        return sum
    }

    fun tiltDirection(direction: Direction, worldMap: MutableMap<Vec2, Char>, maxRows: Int, maxCols: Int) {
        val rowRange = when (direction) {
            Direction.NORTH -> 1..maxRows
            Direction.SOUTH -> (0 until maxRows).reversed()
            Direction.WEST -> 0..maxRows
            Direction.EAST -> 0..maxRows
        }
        val colRange = when (direction) {
            Direction.NORTH -> 0..maxCols
            Direction.SOUTH -> 0..maxCols
            Direction.WEST -> 1..maxCols
            Direction.EAST -> (0 until maxCols).reversed()
        }
        for (row in rowRange) {
            for (col in colRange) {
                if (worldMap[Vec2(row, col)] == 'O') {
                    var currentCoord = Vec2(row, col)
                    val directionToMove = direction.pair
                    var charAbove = worldMap[Vec2(
                        currentCoord.row + directionToMove.first,
                        currentCoord.col + directionToMove.second
                    )] ?: '.'
                    while (charAbove == '.') {
                        worldMap[Vec2(
                            currentCoord.row + directionToMove.first,
                            currentCoord.col + directionToMove.second
                        )] = 'O'
                        worldMap[Vec2(
                            currentCoord.row,
                            currentCoord.col
                        )] = '.'
                        currentCoord = Vec2(
                            currentCoord.row + directionToMove.first,
                            currentCoord.col + directionToMove.second
                        )
                        if ((direction == Direction.NORTH && currentCoord.row == 0) ||
                            (direction == Direction.SOUTH && currentCoord.row == maxRows) ||
                            (direction == Direction.WEST && currentCoord.col == 0) ||
                            (direction == Direction.EAST && currentCoord.col == maxCols)) {
                            break
                        }
                        charAbove = worldMap[Vec2(
                            currentCoord.row + directionToMove.first,
                            currentCoord.col + directionToMove.second
                        ) ] ?: '.'
                    }


                }
            }
        }
    }

    fun part2(input: List<String>): Long {
        val worldMap = input.foldIndexed(mutableMapOf<Vec2, Char>()) { row, acc, next ->
            next.foldIndexed(acc) { col, innerAcc, c ->
                innerAcc[Vec2(row, col)] = c
                innerAcc
            }
            acc
        }
        fun formString(map:MutableMap<Vec2,Char>):String{
            val stringBuilder = StringBuilder()
            for (row in 0..input.lastIndex) {
                for (col in 0..input[0].lastIndex) {
                    stringBuilder.append(worldMap[Vec2(row, col)])
                }
                stringBuilder.append("\n")
            }
            return stringBuilder.toString()
        }

        val storeMap = mutableMapOf<String,String>()
        var initString = formString(worldMap)
        for (i in 1 .. 1000000000) {
            var nextString = storeMap[initString]
            if (nextString.isNullOrBlank()) {
                tiltDirection(Direction.NORTH, worldMap, input.lastIndex, input[0].lastIndex)
                tiltDirection(Direction.WEST, worldMap, input.lastIndex, input[0].lastIndex)
                tiltDirection(Direction.SOUTH, worldMap, input.lastIndex, input[0].lastIndex)
                tiltDirection(Direction.EAST, worldMap, input.lastIndex, input[0].lastIndex)
                nextString = formString(worldMap)
                storeMap[initString] = nextString
            }
            initString=nextString
        }
        val maxRows = input.size
        return initString.lines().foldIndexed(0L){
            row, acc, s ->
            s.fold(acc){
                innerAcc, c ->
                if (c == 'O'){
                    innerAcc + (maxRows-row )
                }else{
                    innerAcc
                }
            }

        }
    }


    val testInput = readInput("Day14_test")
//    check(part1(testInput) == 136L)

    val input = readInput("Day14")
//    println(part1(input))
    println(part2(input))

}