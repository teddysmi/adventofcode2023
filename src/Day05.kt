import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

fun main() {
    data class Mapping(val source: Long, val destination: Long, val range: Long)
    data class Mapper(val sourceLabel: String, val destinationLabel: String, val mappings: List<Mapping>)

    fun mapToDestination(sourceValue: Long, sourceLabel: String, destinationLabel: String, mappers: List<Mapper>): Long {
        var currentSourceLabel = sourceLabel
        var currentSourceValue = sourceValue
        var currentDestinationValue = sourceValue
        var destinationReached = false
        while (!destinationReached) {
            val currentMapper = mappers.filter { it.sourceLabel == currentSourceLabel }[0]
            for (mapping in currentMapper.mappings) {
                if (currentSourceValue in mapping.source until(mapping.source + mapping.range)) {
                    val offset = currentSourceValue - mapping.source
                    currentDestinationValue = mapping.destination + offset
                    break
                }
            }
            if (destinationLabel != currentMapper.destinationLabel) {
                currentSourceLabel = currentMapper.destinationLabel
                currentSourceValue = currentDestinationValue
            } else {
                destinationReached = true
            }
        }
        return currentDestinationValue
    }

    fun mapToSource(destinationValue: Long, sourceLabel: String, destinationLabel: String, mappers: List<Mapper>): Long {
        var currentDestinationLabel = destinationLabel
        var currentSourceValue = destinationValue
        var currentDestinationValue = destinationValue
        var sourceReached = false
        while (!sourceReached) {
            val currentMapper = mappers.filter { it.destinationLabel == currentDestinationLabel }[0]
            for (mapping in currentMapper.mappings) {
                if (currentDestinationValue in mapping.destination until(mapping.destination + mapping.range)) {
                    val offset = currentDestinationValue - mapping.destination
                    currentSourceValue = mapping.source + offset
                    break
                }
            }
            if (sourceLabel != currentMapper.sourceLabel) {
                currentDestinationLabel = currentMapper.sourceLabel
                currentDestinationValue = currentSourceValue
            } else {
                sourceReached = true
            }
        }
        return currentSourceValue
    }

    fun part1(input: List<String>): Long {
        val seeds = input.first().substringAfter(":").split(" ").mapNotNull { it.toLongOrNull() }
        val mappingListString = input.drop(2).fold(mutableListOf(mutableListOf<String>())) { acc, line ->
            if (line.isBlank()) {
                acc.add(mutableListOf())
            } else {
                acc.last().add(line)
            }
            acc
        }
        val mappingObjectList = mappingListString.map {
            val (sourceLabel, _, destinationLabel) = it.first().split("-", " ")
            val mappings = it.drop(1).map { line ->
                val (destination, source, range) = line.split(" ").mapNotNull { s -> s.toLongOrNull() }
                Mapping(source, destination, range)
            }
            Mapper(sourceLabel, destinationLabel, mappings)
        }
        var minLocation = Long.MAX_VALUE
        for (seed in seeds) {
            val location = mapToDestination(seed, "seed", "location", mappingObjectList)
            if (location < minLocation) {
                minLocation = location
            }
        }
        return minLocation
    }

    fun part2(input: List<String>) {
        val seedRanges = input.first().substringAfter(":").split(" ").mapNotNull { it.toLongOrNull() }.windowed(2,2)
//        val seedRanges = listOf(listOf(2361566863L, 262106125L))
        val mappingListString = input.drop(2).fold(mutableListOf(mutableListOf<String>())) { acc, line ->
            if (line.isBlank()) {
                acc.add(mutableListOf())
            } else {
                acc.last().add(line)
            }
            acc
        }
        val mappingObjectList = mappingListString.map {
            val (sourceLabel, _, destinationLabel) = it.first().split("-", " ")
            val mappings = it.drop(1).map { line ->
                val (destination, source, range) = line.split(" ").mapNotNull { s -> s.toLongOrNull() }
                Mapping(source, destination, range)
            }
            Mapper(sourceLabel, destinationLabel, mappings)
        }
        runBlocking(Dispatchers.Default) {
            val smallestList = seedRanges.map { seedRange ->
                async {
                    var minLocation = Long.MAX_VALUE
                    for (seed in seedRange[0] until seedRange[0] + seedRange[1]) {
                        val location = mapToDestination(seed, "seed", "location", mappingObjectList)
                        if (location < minLocation) {
                            minLocation = location
                        }
                    }
                    minLocation
                }

            }.awaitAll()
            println(smallestList)
            println(smallestList.min())
        }
    }

    fun part2Reverse(input: List<String>) {
        val seedRanges = input.first().substringAfter(":").split(" ").mapNotNull { it.toLongOrNull() }.windowed(2, 2)
        val mappingListString = input.drop(2).fold(mutableListOf(mutableListOf<String>())) { acc, line ->
            if (line.isBlank()) {
                acc.add(mutableListOf())
            } else {
                acc.last().add(line)
            }
            acc
        }
        val mappingObjectList = mappingListString.map {
            val (sourceLabel, _, destinationLabel) = it.first().split("-", " ")
            val mappings = it.drop(1).map { line ->
                val (destination, source, range) = line.split(" ").mapNotNull { s -> s.toLongOrNull() }
                Mapping(source, destination, range)
            }
            Mapper(sourceLabel, destinationLabel, mappings)
        }
        println(mapToDestination(2406693241L, "seed", "location", mappingObjectList))
        var currentLocationValue = 0L
        var inSeedRange = false
        var seedValue = 0L
        while (!inSeedRange) {
            seedValue = mapToSource(currentLocationValue, "seed", "location", mappingObjectList)
            for (seedRange in seedRanges) {
                if (seedValue in seedRange[0] until (seedRange[0] + seedRange[1])) {
                    inSeedRange = true
                }
            }
            currentLocationValue++
        }
        println(seedValue)
        println(currentLocationValue - 1)
    }


    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)

    val input = readInput("Day05")
    println(part1(input))
    part2(input)
    part2Reverse(input)

}