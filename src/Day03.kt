fun main() {

    data class PartNumber(val value:Long,val row:Int, val startIndex:Int, val endIndex:Int)
    data class Symbol(val symbol:Char,val row: Int, val column:Int)
    fun List<String>.parse():Pair<List<PartNumber>, List<Symbol>>{
        val partNumberList = mutableListOf<PartNumber>()
        val symbolList = mutableListOf<Symbol>()

        for ((row,line) in this.withIndex()){
            var currentNumber = ""
            line.forEachIndexed { index, c ->
                if (c.isDigit()){
                    currentNumber += c
                }else {
                    if (c != '.') {
                        symbolList.add(Symbol(c, row, index))
                    }
                    if (currentNumber.isNotBlank()) {
                        partNumberList.add(PartNumber(currentNumber.toLong(), row, index - currentNumber.length, index))
                        currentNumber = ""
                    }
                }
            }
            if (currentNumber.isNotBlank()) {
                partNumberList.add(PartNumber(currentNumber.toLong(), row, line.length - currentNumber.length, line.length))
            }
        }
        return Pair(partNumberList,symbolList)
    }

    fun part1(input: List<String>): Long {
        val (partNumberList, symbolList) = input.parse()
        println(symbolList.map{it.symbol})
        return partNumberList.map{
            var numberToSum = 0L
            val rowWindow = ((it.row - 1)..(it.row + 1))
            val indexWindow = ((it.startIndex - 1) .. (it.endIndex))
            for (symbol in symbolList){
                if (symbol.row in rowWindow && symbol.column in indexWindow){
                    numberToSum = it.value
                    break
                }
            }
            numberToSum
        }.sumOf{it}
    }

    fun part2(input: List<String>): Long {
        val (partNumberList, symbolList) = input.parse()
        println(symbolList.map{it.symbol})
        return symbolList.filter{it.symbol=='*'}.map{
            var gearRatio = 0L
            val partInGearList = mutableListOf<Long>()
            val rowWindow = ((it.row - 1)..(it.row + 1))
            for (partNumber in partNumberList){
                val indexWindow = partNumber.startIndex -1..partNumber.endIndex
                if (partNumber.row in rowWindow && it.column in indexWindow){
                    partInGearList.add(partNumber.value)
                }
            }
            if (partInGearList.size == 2){
                gearRatio = partInGearList[0] * partInGearList[1]
            }
            gearRatio
        }.sumOf{it}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part2(testInput) == 467835L)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
