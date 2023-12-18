import java.lang.Long.max
import java.lang.Long.min
import java.util.PriorityQueue

enum class Direction(val pair: Pair<Int, Int>) {
    NORTH(Pair(-1, 0)), EAST(Pair(0, 1)), SOUTH(Pair(1, 0)), WEST(Pair(0, -1));

    fun toBack(): Direction {
        return Direction.values()[(this.ordinal + 2) % 4]
    }
}


fun main() {
    data class Vec2(val row: Int, val col: Int)
    data class Node(val coord: Vec2, val direction: Direction?, val numOfStraight: Int)

    fun findNextPath(
        nextPoint: Vec2,
        visited: Set<Vec2>,
        worldMap: Map<Vec2, Long>,
        goal: Vec2,
        currentDirection: Direction?,
        straightCount: Int,
        cacheMap: MutableMap<Triple<Vec2, Direction, Int>, Pair<Long, Set<Vec2>>>
    ): Pair<Long, Set<Vec2>> {
        if (visited.contains(nextPoint)) {
            return 0L to visited
        }
        val heatAtNextPoint = worldMap[nextPoint] ?: return 0L to visited
//        val newSum = worldMap.entries.fold(0L) { acc, entry ->
//            if (visited.contains(entry.key)) {
//                acc + entry.value.digitToInt().toLong()
//            } else {
//                acc
//            }
//        }
//        if (newSum > goalMinHeat) {
////            println("Stopped at visited length ${visited.size}, current min: $goalMinHeat")
//            return setOf()
//        }
        val newVisited = visited + nextPoint
        if (nextPoint == goal) {

            val heatMap = worldMap.entries.filter { newVisited.contains(it.key) }.sumOf { it.value } to newVisited
            return heatMap
        }
        var currentMinHeat = Int.MAX_VALUE.toLong()
        var currentVisited = newVisited
        for (direction in Direction.values()) {
            if (straightCount >= 2 && direction == currentDirection) {
                continue
            }
            if (currentDirection == direction.toBack()) {
                continue
            }
            val newPoint = Vec2(nextPoint.row + direction.pair.first, nextPoint.col + direction.pair.second)
            val newStraightCount = if (direction == currentDirection) straightCount + 1 else 0
            val (goalHeat, updatedVisited) = cacheMap.getOrElse(Triple(newPoint, direction, newStraightCount)) {
                findNextPath(
                    newPoint, newVisited, worldMap, goal, direction, newStraightCount, cacheMap
                )
            }
            cacheMap[Triple(newPoint, direction, newStraightCount)] = goalHeat to updatedVisited
            if (goalHeat != 0L && goalHeat < currentMinHeat) {
                currentMinHeat = goalHeat
                currentVisited = updatedVisited
            }
        }
        return (currentMinHeat) to currentVisited
    }

    fun getSum(worldMap: MutableMap<Vec2, Long>, path: List<Vec2>): Long {
        return worldMap.entries.filter { path.contains(it.key) }.sumOf { it.value }
    }


    fun part1(input: List<String>): Long {
        val worldMap = input.foldIndexed(mutableMapOf<Vec2, Long>()) { row, acc, s ->
            s.foldIndexed(acc) { col, innerAcc, c ->
                innerAcc[Vec2(row, col)] = c.digitToInt().toLong()
                innerAcc
            }
            acc
        }
        val startingPoint = Vec2(0, 0)
        val endPoint = Vec2(input.lastIndex, input[0].lastIndex)
        val visited = mutableSetOf<Node>()
        val nodeQueue = PriorityQueue<Pair<Node, Long>> { c1, c2 ->
            (c1.second - c2.second).toInt()
        }
        nodeQueue.add(Node(startingPoint, Direction.EAST, 0) to 0)
        nodeQueue.add(Node(startingPoint, Direction.SOUTH, 0) to 0)
        while (nodeQueue.isNotEmpty()) {
            val currentNode = nodeQueue.remove()
            if (visited.contains(currentNode.first)){
                continue
            }
            visited.add(currentNode.first)
            println(currentNode.first.coord)
            if (currentNode.first.coord == endPoint) {
                println(currentNode.second)
                return currentNode.second
            }
            for (direction in Direction.values()) {
                if (currentNode.first.numOfStraight >= 2 && direction == currentNode.first.direction) {
                    continue
                }
                if (currentNode.first.direction == direction.toBack()) {
                    continue
                }
                val nextPoint = Vec2(
                    currentNode.first.coord.row + direction.pair.first,
                    currentNode.first.coord.col + direction.pair.second
                )
                val costOfNextPoint = worldMap[nextPoint] ?: continue
                val currentNumOfStraight =
                    if (direction == currentNode.first.direction) currentNode.first.numOfStraight + 1 else 0
                val existingNode =
                    nodeQueue.find { it.first.coord == nextPoint && it.first.direction == direction && it.first.numOfStraight == currentNumOfStraight }
                if (existingNode != null) {
                    if (existingNode.second > currentNode.second + costOfNextPoint) {
                        nodeQueue.remove(existingNode)

                        nodeQueue.add(
                            existingNode.first to
                                    currentNode.second + costOfNextPoint
                        )

                    }
                } else {
                    nodeQueue.add(
                        Node(
                            nextPoint,
                            direction,
                            currentNumOfStraight
                        ) to currentNode.second + costOfNextPoint
                    )
                }
            }
        }
        return 0L
    }

    fun part2(input: List<String>): Long {
        val worldMap = input.foldIndexed(mutableMapOf<Vec2, Long>()) { row, acc, s ->
            s.foldIndexed(acc) { col, innerAcc, c ->
                innerAcc[Vec2(row, col)] = c.digitToInt().toLong()
                innerAcc
            }
            acc
        }
        val startingPoint = Vec2(0, 0)
        val endPoint = Vec2(input.lastIndex, input[0].lastIndex)
        val visited = mutableSetOf<Node>()
        val nodeQueue = PriorityQueue<Pair<Node, Long>> { c1, c2 ->
            (c1.second - c2.second).toInt()
        }
        nodeQueue.add(Node(startingPoint, null, 0) to 0)
        while (nodeQueue.isNotEmpty()) {
            val currentNode = nodeQueue.remove()
            if (visited.contains(currentNode.first)){
                continue
            }
            visited.add(currentNode.first)
            println(currentNode.first.coord)
            if (currentNode.first.coord == endPoint) {
                println(currentNode.second)
                if (currentNode.first.numOfStraight < 3){
                    continue
                }
                return currentNode.second
            }
            for (direction in Direction.values()) {

                if (currentNode.first.numOfStraight >= 9 && direction == currentNode.first.direction) {
                    continue
                }
                if (currentNode.first.coord != startingPoint &&  currentNode.first.numOfStraight < 3 && direction != currentNode.first.direction){
                    continue
                }
                if (currentNode.first.direction == direction.toBack()) {
                    continue
                }
                val nextPoint = Vec2(
                    currentNode.first.coord.row + direction.pair.first,
                    currentNode.first.coord.col + direction.pair.second
                )
                val costOfNextPoint = worldMap[nextPoint] ?: continue
                val currentNumOfStraight =
                    if (direction == currentNode.first.direction) currentNode.first.numOfStraight + 1 else 0
                val existingNode =
                    nodeQueue.find { it.first.coord == nextPoint && it.first.direction == direction && it.first.numOfStraight == currentNumOfStraight }
                if (existingNode != null) {
                    if (existingNode.second > currentNode.second + costOfNextPoint) {
                        nodeQueue.remove(existingNode)

                        nodeQueue.add(
                            existingNode.first to
                                    currentNode.second + costOfNextPoint
                        )

                    }
                } else {
                    nodeQueue.add(
                        Node(
                            nextPoint,
                            direction,
                            currentNumOfStraight
                        ) to currentNode.second + costOfNextPoint
                    )
                }
            }
        }
        return 0L
    }


    val testInput = readInput("Day17_test")
//    check(part1(testInput) == 102L)

    val input = readInput("Day17")
//    println(part1(input))
    println(part2(input))

}