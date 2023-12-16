import java.lang.Long.max

enum class Direction(val pair: Pair<Int, Int>) {
    NORTH(Pair(-1, 0)),
    EAST(Pair(0, 1)),
    SOUTH(Pair(1, 0)),
    WEST(Pair(0, -1))
}

fun main() {
    data class Vec2(val row: Int, val col: Int)

    data class Beam(val coordinates: Vec2, val direction: Direction)

    fun checkNextBeam(
        currentBeam: Beam,
        worldMap: MutableMap<Vec2, Char>,
        energizedCoordinates: MutableSet<Vec2>,
        visited: MutableSet<Pair<Vec2, Direction>>
    ) {
        val currentBeamCoordinates = currentBeam.coordinates
        val currentBeamDirection = currentBeam.direction
        val nextBeamCoordinates = Vec2(
            currentBeamCoordinates.row + currentBeamDirection.pair.first,
            currentBeamCoordinates.col + currentBeamDirection.pair.second
        )
        when (worldMap[nextBeamCoordinates]) {
            '.' -> {
                energizedCoordinates.add(nextBeamCoordinates)
                if (visited.contains(Pair(nextBeamCoordinates, currentBeamDirection))) {
                    return
                }
                visited.add(nextBeamCoordinates to currentBeamDirection)
                checkNextBeam(Beam(nextBeamCoordinates, currentBeamDirection), worldMap, energizedCoordinates, visited)

            }

            '/' -> {
                energizedCoordinates.add(nextBeamCoordinates)
                val nextDirection = when (currentBeamDirection) {
                    Direction.NORTH -> Direction.EAST
                    Direction.EAST -> Direction.NORTH
                    Direction.SOUTH -> Direction.WEST
                    Direction.WEST -> Direction.SOUTH
                }
                if (visited.contains(Pair(nextBeamCoordinates, nextDirection))) {
                    return
                }
                visited.add(nextBeamCoordinates to nextDirection)
                checkNextBeam(Beam(nextBeamCoordinates, nextDirection), worldMap, energizedCoordinates, visited)
            }

            '\\' -> {
                energizedCoordinates.add(nextBeamCoordinates)
                val nextDirection = when (currentBeamDirection) {
                    Direction.NORTH -> Direction.WEST
                    Direction.EAST -> Direction.SOUTH
                    Direction.SOUTH -> Direction.EAST
                    Direction.WEST -> Direction.NORTH
                }
                if (visited.contains(Pair(nextBeamCoordinates, nextDirection))) {
                    return
                }
                visited.add(nextBeamCoordinates to nextDirection)
                checkNextBeam(Beam(nextBeamCoordinates, nextDirection), worldMap, energizedCoordinates, visited)
            }

            '-' -> {
                energizedCoordinates.add(nextBeamCoordinates)
                when (currentBeamDirection) {
                    Direction.NORTH, Direction.SOUTH -> {
                        if (!visited.contains(Pair(nextBeamCoordinates, currentBeamDirection))) {
                            visited.add(nextBeamCoordinates to Direction.WEST)
                            checkNextBeam(
                                Beam(nextBeamCoordinates, Direction.WEST),
                                worldMap,
                                energizedCoordinates,
                                visited
                            )
                        }
                        if (!visited.contains(Pair(nextBeamCoordinates, currentBeamDirection))) {
                            visited.add(nextBeamCoordinates to Direction.EAST)
                            checkNextBeam(
                                Beam(nextBeamCoordinates, Direction.EAST),
                                worldMap,
                                energizedCoordinates,
                                visited
                            )
                        }
                    }
                    Direction.EAST, Direction.WEST -> {
                        if (visited.contains(Pair(nextBeamCoordinates, currentBeamDirection))) {
                            return
                        }
                        visited.add(nextBeamCoordinates to currentBeamDirection)
                        checkNextBeam(
                            Beam(nextBeamCoordinates, currentBeamDirection),
                            worldMap,
                            energizedCoordinates,
                            visited
                        )
                    }
                }
            }

            '|' -> {
                energizedCoordinates.add(nextBeamCoordinates)
                when (currentBeamDirection) {
                    Direction.EAST, Direction.WEST -> {
                        if (!visited.contains(Pair(nextBeamCoordinates, currentBeamDirection))) {
                            visited.add(nextBeamCoordinates to Direction.NORTH)
                            checkNextBeam(
                                Beam(nextBeamCoordinates, Direction.NORTH),
                                worldMap,
                                energizedCoordinates,
                                visited
                            )
                        }
                        if (!visited.contains(Pair(nextBeamCoordinates, currentBeamDirection))) {
                            visited.add(nextBeamCoordinates to Direction.SOUTH)
                            checkNextBeam(
                                Beam(nextBeamCoordinates, Direction.SOUTH),
                                worldMap,
                                energizedCoordinates,
                                visited
                            )
                        }
                    }
                    Direction.NORTH, Direction.SOUTH -> {
                        if (visited.contains(Pair(nextBeamCoordinates, currentBeamDirection))) {
                            return
                        }
                        visited.add(nextBeamCoordinates to currentBeamDirection)
                        checkNextBeam(
                            Beam(nextBeamCoordinates, currentBeamDirection),
                            worldMap,
                            energizedCoordinates,
                            visited
                        )
                    }
                }
            }

            else -> return
        }
    }

    fun part1(input: List<String>): Long {
        val worldMap = input.foldIndexed(mutableMapOf<Vec2, Char>()) { row, acc, s ->
            s.foldIndexed(acc) { col, innerAcc, c ->
                innerAcc[Vec2(row, col)] = c
                innerAcc
            }
            acc
        }

        val initialBeam = Beam(Vec2(0, -1), Direction.EAST)
        val energizedCoordinates = mutableSetOf<Vec2>()
        val visitedCoordinatesAndDirection = mutableSetOf<Pair<Vec2, Direction>>()
        checkNextBeam(initialBeam, worldMap, energizedCoordinates, visitedCoordinatesAndDirection)
        return energizedCoordinates.size.toLong()
    }

    fun part2(input: List<String>): Long {
        val worldMap = input.foldIndexed(mutableMapOf<Vec2, Char>()) { row, acc, s ->
            s.foldIndexed(acc) { col, innerAcc, c ->
                innerAcc[Vec2(row, col)] = c
                innerAcc
            }
            acc
        }
        val initialBeamSet = mutableSetOf<Beam>()
        for (row in 0..input.lastIndex){
            for (col in 0 .. input[0].lastIndex){
                if (col == 0){
                    initialBeamSet.add(Beam(Vec2(row,col-1),Direction.EAST))
                }
                if(col == input[0].lastIndex){
                    initialBeamSet.add(Beam(Vec2(row,col+1),Direction.WEST))
                }
                if (row == 0){
                    initialBeamSet.add(Beam(Vec2(row-1,col),Direction.SOUTH))
                }
                if(row==input.lastIndex){
                    initialBeamSet.add(Beam(Vec2(row+1,col),Direction.NORTH))
                }
            }
        }
        var maxEnergized = 0L
        for(initialBeam in initialBeamSet) {
            val energizedCoordinates = mutableSetOf<Vec2>()
            val visitedCoordinatesAndDirection = mutableSetOf<Pair<Vec2, Direction>>()
            checkNextBeam(initialBeam, worldMap, energizedCoordinates, visitedCoordinatesAndDirection)
            maxEnergized = max(energizedCoordinates.size.toLong(),maxEnergized)
        }
        return maxEnergized
    }


    val testInput = readInput("Day16_test")
    check(part2(testInput) == 51L)

    val input = readInput("Day16")
//    println(part1(input))
    println(part2(input))

}