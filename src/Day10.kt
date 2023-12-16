enum class DIRECTION() {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

enum class PIPES_IN_DIRECTION(val pipeList: List<Pair<Char, DIRECTION>>) {
    PIPES_UP(listOf('|' to DIRECTION.UP, '7' to DIRECTION.LEFT, 'F' to DIRECTION.RIGHT)),
    PIPES_DOWN(listOf('|' to DIRECTION.DOWN, 'L' to DIRECTION.RIGHT, 'J' to DIRECTION.LEFT)),
    PIPES_LEFT(listOf('-' to DIRECTION.LEFT, 'L' to DIRECTION.UP, 'F' to DIRECTION.DOWN)),
    PIPES_RIGHT(listOf('-' to DIRECTION.RIGHT, '7' to DIRECTION.DOWN, 'J' to DIRECTION.UP))
}

fun main() {


//    val pipeMap = mapOf('|' to Pair(1, 0), '-' to Pair(0, 1), 'L' to Pair(1, 1), 'J' to Pair(1, -1), '7' to Pair(-1, -1), 'F' to Pair(-1, 1))

    fun checkNextInDirection(direction: DIRECTION, currentPosition: Pair<Int, Int>, map: Map<Pair<Int, Int>, Char>): Char {
        return when (direction) {
            DIRECTION.UP -> map[Pair(currentPosition.first - 1, currentPosition.second)] ?: 'P'
            DIRECTION.DOWN -> map[Pair(currentPosition.first + 1, currentPosition.second)] ?: 'P'
            DIRECTION.LEFT -> map[Pair(currentPosition.first, currentPosition.second - 1)] ?: 'P'
            DIRECTION.RIGHT -> map[Pair(currentPosition.first, currentPosition.second + 1)] ?: 'P'
        }
    }

    fun getNumberOfPoints(loop:MutableList<Pair<Int,Int>>):Long{
        val modifiedLoop = loop + loop.first()
        val area = modifiedLoop.windowed(2,1).fold(0L){
            acc,next->
            val pointOne = next[0]
            val pointTwo = next[1]
            acc + (pointOne.first * pointTwo.second) - (pointOne.second * pointTwo.first)
        } / 2
        return area - (loop.size/2) + 1L
    }

    fun part1(input: List<String>): Long {
        val pipeMap = input.foldIndexed(mutableMapOf<Pair<Int, Int>, Char>()) { row, acc, s ->
            s.forEachIndexed { col, it ->
                acc[Pair(row, col)] = it
            }
            acc
        }
        val sPosition = pipeMap.filter { it.value == 'S' }
        var currentPosition = sPosition.keys.first()
        var nextDirection = DIRECTION.UP
        for (direction in DIRECTION.values()) {
            val nextChar = checkNextInDirection(direction, currentPosition, pipeMap)
            val pipesList = PIPES_IN_DIRECTION.values()[direction.ordinal].pipeList
            val filteredPipeList = pipesList.filter { it.first == nextChar }
            if (filteredPipeList.isNotEmpty()) {
                currentPosition = when (direction) {
                    DIRECTION.UP -> Pair(currentPosition.first - 1, currentPosition.second)
                    DIRECTION.DOWN -> Pair(currentPosition.first + 1, currentPosition.second)
                    DIRECTION.LEFT -> Pair(currentPosition.first, currentPosition.second - 1)
                    DIRECTION.RIGHT -> Pair(currentPosition.first, currentPosition.second + 1)
                }
                nextDirection = filteredPipeList[0].second
                break
            }
        }
        var numberOfSteps = 1L
        var currentCharacter = 'P'
        while (currentCharacter != 'S') {
            currentCharacter = checkNextInDirection(nextDirection, currentPosition, pipeMap)
            currentPosition = when (nextDirection) {
                DIRECTION.UP -> Pair(currentPosition.first - 1, currentPosition.second)
                DIRECTION.DOWN -> Pair(currentPosition.first + 1, currentPosition.second)
                DIRECTION.LEFT -> Pair(currentPosition.first, currentPosition.second - 1)
                DIRECTION.RIGHT -> Pair(currentPosition.first, currentPosition.second + 1)
            }
            val pipesList = PIPES_IN_DIRECTION.values()[nextDirection.ordinal].pipeList
            val filteredPipeList = pipesList.filter { it.first == currentCharacter }
            if (filteredPipeList.isNotEmpty()) {
                nextDirection = filteredPipeList[0].second
            }
            numberOfSteps++
        }
        return numberOfSteps / 2
    }

    fun part2(input: List<String>): Long {
        val pipeMap = input.foldIndexed(mutableMapOf<Pair<Int, Int>, Char>()) { row, acc, s ->
            s.forEachIndexed { col, it ->
                acc[Pair(row, col)] = it
            }
            acc
        }
        val positionsInLoop = mutableListOf<Pair<Int, Int>>()
        val sPosition = pipeMap.filter { it.value == 'S' }
        var currentPosition = sPosition.keys.first()
        positionsInLoop.add(currentPosition)
        var nextDirection = DIRECTION.UP
        for (direction in DIRECTION.values()) {
            val nextChar = checkNextInDirection(direction, currentPosition, pipeMap)
            val pipesList = PIPES_IN_DIRECTION.values()[direction.ordinal].pipeList
            val filteredPipeList = pipesList.filter { it.first == nextChar }
            if (filteredPipeList.isNotEmpty()) {
                currentPosition = when (direction) {
                    DIRECTION.UP -> Pair(currentPosition.first - 1, currentPosition.second)
                    DIRECTION.DOWN -> Pair(currentPosition.first + 1, currentPosition.second)
                    DIRECTION.LEFT -> Pair(currentPosition.first, currentPosition.second - 1)
                    DIRECTION.RIGHT -> Pair(currentPosition.first, currentPosition.second + 1)
                }
                nextDirection = filteredPipeList[0].second
                break
            }
        }

        positionsInLoop.add(currentPosition)
        var numberOfSteps = 1L
        var currentCharacter = 'P'
        while (currentCharacter != 'S') {
            currentCharacter = checkNextInDirection(nextDirection, currentPosition, pipeMap)
            currentPosition = when (nextDirection) {
                DIRECTION.UP -> Pair(currentPosition.first - 1, currentPosition.second)
                DIRECTION.DOWN -> Pair(currentPosition.first + 1, currentPosition.second)
                DIRECTION.LEFT -> Pair(currentPosition.first, currentPosition.second - 1)
                DIRECTION.RIGHT -> Pair(currentPosition.first, currentPosition.second + 1)
            }
            val pipesList = PIPES_IN_DIRECTION.values()[nextDirection.ordinal].pipeList
            val filteredPipeList = pipesList.filter { it.first == currentCharacter }
            if (filteredPipeList.isNotEmpty()) {
                nextDirection = filteredPipeList[0].second
                positionsInLoop.add(currentPosition)
            }
            numberOfSteps++
        }
        val modifiedMap = pipeMap.entries.fold(mutableMapOf<Pair<Int, Int>, Char>()) { acc, next ->
            if (positionsInLoop.contains(next.key)) {
                acc[next.key] = next.value
            } else {
                acc[next.key] = '.'
            }
            acc
        }

        print(getNumberOfPoints(positionsInLoop))

        val positionsIn = mutableSetOf<Pair<Int, Int>>()
        val positionsOut = mutableSetOf<Pair<Int, Int>>()
        var numberOfIn = 0L
        for (row in input.indices) {
            for (col in input[0].indices) {
                if (modifiedMap[Pair(row, col)] == '.') {
                    var numberOfWalls = 0
                    var edgeCheck = '9'
                    for (checkCol in col..input[0].lastIndex) {
                        val currentChar = modifiedMap[Pair(row, checkCol)]
                        if (currentChar != '.' && currentChar != '-' && currentChar != 'S') {
                            if (currentChar == 'L' || currentChar == 'F') {
                                edgeCheck = currentChar
                                numberOfWalls++
                            } else if (currentChar == '7' && edgeCheck == 'L') {
                                edgeCheck = '9'
                            } else if (currentChar == 'J' && edgeCheck == 'F'){
                                edgeCheck = '9'
                            } else {
                                numberOfWalls++
                            }
                        }
                    }
                    if (numberOfWalls % 2 == 1) {
                        positionsIn.add(Pair(row, col))
                        numberOfIn++
                    } else {
                        positionsOut.add(Pair(row, col))
                    }
                }
            }
        }

        for (row in input.indices) {
            var currentRow = ""
            for (col in input[0].indices) {
                if (positionsIn.contains(Pair(row, col))) {
                    currentRow += 'I'
                } else if (positionsOut.contains(Pair(row, col))) {
                    currentRow += 'O'
                } else {
                    currentRow += modifiedMap[Pair(row, col)] ?: ""
                }
            }
            println(currentRow)
        }

        return numberOfIn
    }


    val testInput = readInput("Day10_test")
//    check(part1(testInput) == 8L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))

}