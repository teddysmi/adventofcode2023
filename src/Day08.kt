import java.lang.Long.max

fun main() {
    fun part1(input: List<String>): Long {
        val route = input[0]

        val map = input.drop(2).fold(mutableMapOf<String,Pair<String,String>>()){
            acc, next ->
            val (key, value) = next.split("=").map{it.trim()}
            val pairList = value.substringAfter("(").substringBefore(")").split(",")
            acc[key] = Pair(pairList[0].trim(),pairList[1].trim())
            acc
        }
        var currentPoint = "AAA"
        var currentIndex = 0
        var numberOfSteps = 0L
        while(currentPoint!="ZZZ"){
            val path = route[currentIndex]
            if (path == 'R'){
                currentPoint = map[currentPoint]?.second ?: ""
            }else{
                currentPoint = map[currentPoint]?.first ?: ""
            }
            if (currentIndex == route.lastIndex){
                currentIndex = 0
            }else {
                currentIndex++
            }
            numberOfSteps++
        }
        return numberOfSteps
    }

    fun part2(input: List<String>): Long {
        val route = input[0]
        val startingPoints = input.drop(2).map{it.substringBefore("=").trim()}.filter { it.last() =='A' }
        val map = input.drop(2).fold(mutableMapOf<String,Pair<String,String>>()){
            acc, next ->
            val (key, value) = next.split("=").map{it.trim()}
            val pairList = value.substringAfter("(").substringBefore(")").split(",")
            acc[key] = Pair(pairList[0].trim(),pairList[1].trim())
            acc
        }
        val numberOfStepsList = startingPoints.map {
            var currentPoint = it
            var currentIndex = 0
            var numberOfSteps = 0L
            while (currentPoint.last() != 'Z') {
                val path = route[currentIndex]
                currentPoint = if (path == 'R') {
                    map[currentPoint]?.second ?: ""
                } else {
                    map[currentPoint]?.first ?: ""
                }
                if (currentIndex == route.lastIndex) {
                    currentIndex = 0
                } else {
                    currentIndex++
                }
                numberOfSteps++
            }
            numberOfSteps
        }

        return numberOfStepsList.fold(1L){
            acc,next->
            val higher = max(acc,next)
            var lms = higher
            while(lms%acc!=0L || lms%next!=0L){
                lms+=higher
            }
            lms
        }
    }


    val testInput = readInput("Day08_test")
    check(part2(testInput) == 6L)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))

}