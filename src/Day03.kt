fun main() {
    fun part1(input: List<String>): Long {
        val counts = List(input[0].length){0}
        val gamma = input.fold(counts){ acc: List<Int>, s: String ->
            acc.zip(s.toList()).map{pair -> if(pair.second == '1') pair.first+1 else pair.first }
        }.map{i: Int -> if (i > input.size - i) "1" else "0" }
        val epsilon = gamma.map{if(it == "1") "0" else "1"}

        return gamma.joinToString("").toLong(2)*epsilon.joinToString("").toLong(2)
    }

    fun part2(input: List<String>): Long {
        val gamma = input[0].foldIndexed(input){index:Int, acc: List<String>, _: Char ->
            val counts = acc.fold(0){ count:Int, item:String ->
                if (item.toList()[index] == '1') count +1 else count
            }
            val mostFreqBit = if (counts >= acc.size-counts) {
                '1'
            }else{
                '0'
            }
            acc.mapNotNull {

                if(it.toList()[index] == mostFreqBit) it else null
            }
        }
        val epsilon = input[0].foldIndexed(input){index:Int, acc: List<String>, _: Char ->
            val counts = acc.fold(0){ count:Int, item:String ->
                if (item.toList()[index] == '1') count +1 else count
            }
            val leastFreqBit = if (counts < acc.size-counts) {
                '1'
            }else{
                '0'
            }
            if (acc.size == 1){
                acc
            }else{

                acc.mapNotNull {

                    if(it.toList()[index] == leastFreqBit) it else null
                }
            }
        }
        return gamma.joinToString("").toLong(2)*epsilon.joinToString("").toLong(2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part2(testInput) == 230L)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
