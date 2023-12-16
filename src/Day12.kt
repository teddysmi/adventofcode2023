import java.lang.RuntimeException

fun main() {

    data class Configuration(val unknownPositions: List<Int>, val patternString: String, val desiredConfig: List<Int>)


    fun isValid(pattern: String, config: List<Int>): Boolean {
        val groupedPattern = pattern.split('.').filter { it.contains('#') }.map { it.length }
        return groupedPattern == config
    }

    fun part1(input: List<String>): Long {
        val configurationList = input.map {
            val (springString, configString) = it.split(" ")
            val config = configString.split(",").map { c -> c.toInt() }
            val unknownPositions = springString.foldIndexed(mutableListOf<Int>()) { index, acc, next ->
                if (next == '?') {
                    acc.add(index)
                }
                acc
            }
            Configuration(unknownPositions, springString, config)
        }
        val numberOfWays = configurationList.map { configuration ->
            var sum = 0
            var startInt = 0
            var start = Integer.toBinaryString(startInt)
            val unknownPositionSize = configuration.unknownPositions.size
            while (start.length <= unknownPositionSize) {
                val replacementList =
                    configuration.unknownPositions.zip(start.padStart(unknownPositionSize, '0').toList())
                val patternChars = configuration.patternString.toCharArray()
                replacementList.forEach {
                    patternChars[it.first] = if (it.second == '0') '.' else '#'
                }
                val replacedString = String(patternChars)
                if (isValid(replacedString, configuration.desiredConfig)) {
                    sum += 1
                }
                startInt++
                start = Integer.toBinaryString(startInt)
            }
            sum
        }
        println(numberOfWays)
        return numberOfWays.sum().toLong()

    }

    fun part2(input: List<String>): Long {
        val configurationList = input.map {
            val (springString, configString) = it.split(" ")
            val config = configString.split(",").map { c -> c.toInt() }
            val unknownPositions = springString.foldIndexed(mutableListOf<Int>()) { index, acc, next ->
                if (next == '?') {
                    acc.add(index)
                }
                acc
            }
            Configuration(unknownPositions, springString, config)
        }

        val modifiedConfigurationList = configurationList.map{
            val originalPattern = it.patternString
            val originalConfig = it.desiredConfig
            val modifiedPattern = List(5){originalPattern}.joinToString("?")
            val modifiedConfig = List(5){originalConfig}.flatten()
            Configuration(listOf(),modifiedPattern,modifiedConfig)
        }

        val cacheMap = mutableMapOf<Pair<String, List<Int>>, Long>()

        fun checkPossibility(pattern: String, config: List<Int>): Long {
            if (config.isEmpty()) {
                return if (pattern.contains('#')) {
                    cacheMap[pattern to config] = 0L
                    0L
                } else {
                    cacheMap[pattern to config] = 1L
                    1L
                }
            }
            if (pattern.isBlank()) {
                cacheMap[pattern to config] = 0L
                return 0L
            }
            val nextChar = pattern[0]
            val nextConfig = config[0]

            fun checkBroken(): Long {
                val subStringToCheck = pattern.substring(0, nextConfig.coerceAtMost(pattern.length)).replace("?", "#")
                if (subStringToCheck != ("#".repeat(nextConfig))) {
                    cacheMap[pattern to config] = 0L
                    return 0
                }
                if (pattern.length == nextConfig) {
                    return if (config.size == 1) {
                        cacheMap[pattern to config] = 1L
                        1
                    } else {
                        cacheMap[pattern to config] = 0L
                        0
                    }
                }
                if (pattern[nextConfig] !in "?.") {
                    cacheMap[pattern to config] = 0L
                    return 0
                }
                val numberOfWays = (cacheMap.getOrElse(
                    pattern.substring((nextConfig + 1).coerceAtMost(pattern.length)) to config.drop(1)
                ) {
                    checkPossibility(
                        pattern.substring((nextConfig + 1).coerceAtMost(pattern.length)),
                        config.drop(1)
                    )
                })
                cacheMap[pattern.substring((nextConfig + 1).coerceAtMost(pattern.length)) to config.drop(1)] =
                    numberOfWays
                return numberOfWays
            }

            return when (nextChar) {
                '.' -> {
                    val numberOfWays =
                        (cacheMap.getOrElse(pattern.drop(1) to config) { checkPossibility(pattern.drop(1), config) })
                    cacheMap[Pair(pattern.drop(1), config)] = numberOfWays
                    return numberOfWays
                }

                '#' -> checkBroken()
                '?' -> {
                    val dotCase = (cacheMap.getOrElse(pattern.drop(1) to config) {
                        checkPossibility(
                            pattern.drop(1),
                            config
                        )
                    })
                    cacheMap[pattern.drop(1) to config] = dotCase
                    val brokenCase = checkBroken()
                    return dotCase + brokenCase
                }

                else -> throw RuntimeException()
            }

        }

        val sumList = mutableListOf<Long>()
        for (configuration in modifiedConfigurationList) {
            sumList.add(checkPossibility(configuration.patternString, configuration.desiredConfig))
        }

        println(sumList)
        return sumList.sum()
    }


    val testInput = readInput("Day12_test")
    check(part2(testInput) == 525152L)

    val input = readInput("Day12")
//    println(part1(input))
    println(part2(input))

}