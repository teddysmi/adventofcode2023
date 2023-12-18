import kotlin.math.absoluteValue

enum class InstructionsDirection(val pair: Pair<Int, Int>) {
    R(Pair(0, 1)), D(Pair(1, 0)), L(Pair(0, -1)), U(Pair(-1, 0));

    fun toBack(): InstructionsDirection {
        return InstructionsDirection.values()[(this.ordinal + 2) % 4]
    }
}

fun main() {

    data class Vec2(val row: Long, val col: Long)

    fun getArea(vertices: List<Vec2>): Long {
        return (vertices.windowed(2, 1).fold(0L) { acc, vec2s ->
            val (v1, v2) = vec2s
            acc + ((v1.row * v2.col) - (v2.row * v1.col))
        } * 0.5).toLong()
    }

    fun part1(input: List<String>): Long {
        val instructions = input.fold(listOf<Pair<String, Int>>()) { acc, s ->
            val (direction, length) = s.split(" ")
            acc + Pair(direction, length.toInt())
        }
        val worldMap = mutableListOf<Pair<Vec2, Char>>()
        var currentCoordinate = Vec2(0, 0)
        worldMap.add(currentCoordinate to '#')
        for (instruction in instructions) {
            val currentDirection = InstructionsDirection.valueOf(instruction.first)
            for (i in 0 until instruction.second) {
                val newCoordinate = Vec2(
                    currentCoordinate.row + currentDirection.pair.first,
                    currentCoordinate.col + currentDirection.pair.second
                )
                worldMap.add(newCoordinate to '#')
                currentCoordinate = newCoordinate
            }
        }
        val coordinates = worldMap.map { it.first }
        val area = getArea(coordinates).absoluteValue
        val interiorPoints = area + 1 - coordinates.toSet().size * 0.5
        return (interiorPoints + coordinates.toSet().size).toLong()
    }

    fun part2(input: List<String>): Long {
        val instructions = input.fold(listOf<Pair<String, Long>>()) { acc, s ->
            val (_, _, encodedInstruction) = s.split(" ")
            val trimmedEncodedInstruction = encodedInstruction.substringAfter('#').substringBefore(')')
            val length = trimmedEncodedInstruction.take(5)
            val direction = trimmedEncodedInstruction.last()
            acc + Pair(InstructionsDirection.values()[direction.digitToInt()].name, length.toLong(radix = 16))
        }
        val worldMap = mutableListOf<Pair<Vec2, Char>>()
        var currentCoordinate = Vec2(0, 0)
        worldMap.add(currentCoordinate to '#')
        var totalLength = 0L
        for (instruction in instructions) {
            val currentDirection = InstructionsDirection.valueOf(instruction.first)
            val range = instruction.second
            totalLength += range
            val newCoordinate = Vec2(
                currentCoordinate.row + (range * currentDirection.pair.first),
                currentCoordinate.col + (range * currentDirection.pair.second)
            )
            worldMap.add(newCoordinate to '#')
            currentCoordinate = newCoordinate

        }
        val coordinates = worldMap.map { it.first }
        val area = getArea(coordinates).absoluteValue
        val interiorPoints = area + 1 - totalLength * 0.5
        return (interiorPoints + totalLength).toLong()
    }


    val testInput = readInput("Day18_test")
    check(part2(testInput) == 952408144115L)

    val input = readInput("Day18")
//    println(part1(input))
    println(part2(input))

}