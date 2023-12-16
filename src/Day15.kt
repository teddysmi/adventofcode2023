fun main() {

    data class Lens(val label: String, val focalLength: Int)

    fun part1(input: List<String>): Long {

        return input[0].split(",").fold(0L) { acc, s ->
            acc + s.fold(0L) { innerAcc, c ->
                ((innerAcc + c.code) * 17) % 256
            }
        }
    }

    fun part2(input: List<String>): Long {
        val boxMap = mutableMapOf<Int, MutableList<Lens>>()
        for (i in 0..255) {
            boxMap[i] = mutableListOf()
        }
        input[0].split(",").forEach {
            if (it.contains("=")) {
                val label = it.substringBefore("=")
                val focalLength = it.substringAfter("=").trim().toInt()
                val boxNumber = label.fold(0) { acc, c ->
                    ((acc + c.code) * 17) % 256
                }
                val lensList = boxMap[boxNumber]
                var inserted = false
                if (lensList != null) {
                    for (i in lensList.indices) {
                        if (lensList[i].label == label) {
                            lensList[i] = Lens(label,focalLength)
                            inserted = true
                            break
                        }
                    }
                    if (!inserted){
                        lensList.add(Lens(label,focalLength))
                    }
                }

            } else if (it.contains("-")) {
                val label = it.substringBefore("-")
                val boxNumber = label.fold(0) { acc, c ->
                    ((acc + c.code) * 17) % 256
                }
                val lensList = boxMap[boxNumber]
                if (lensList != null) {
                    for (lens in lensList) {
                        if (lens.label == label) {
                            lensList.remove(lens)
                            break
                        }
                    }
                }
            }
        }

        return boxMap.entries.fold(0L) { acc, mutableEntry ->
            val key = mutableEntry.key
            mutableEntry.value.foldIndexed(acc) { index, innerAcc, lens ->
                innerAcc + ((key+1) * (index + 1) * lens.focalLength)
            }
        }
    }


    val testInput = readInput("Day15_test")
    check(part2(testInput) == 145L)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))

}