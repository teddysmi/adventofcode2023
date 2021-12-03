fun main() {
    fun part1(input: List<String>): Int {

        return input.fold(listOf(0,0)){ acc: List<Int>, s:String->
            val a = s.split(" ")
            when (a[0]) {
                "forward" -> return@fold acc.mapIndexed{idx, value->
                    if(idx==0)value+a[1].toInt() else value
                }
                "down" -> return@fold acc.mapIndexed{idx, value->
                    if(idx==1)value+a[1].toInt() else value
                }
                "up" -> return@fold acc.mapIndexed{idx, value->
                    if(idx==1)value-a[1].toInt() else value
                }
                else -> acc
            }

        }.reduce{acc,it -> acc*it}

    }

    fun part2(input: List<String>): Int {

        return input.fold(listOf(0,0,0)){ acc: List<Int>, s:String->
            val a = s.split(" ")
            when (a[0]) {
                "forward" -> return@fold acc.mapIndexed{idx, value->
                    if(idx==0)value+a[1].toInt()
                    else if (idx==1) value+a[1].toInt()*acc[2]
                    else value
                }
                "down" -> return@fold acc.mapIndexed{idx, value->
                    if(idx==2)value+a[1].toInt() else value
                }
                "up" -> return@fold acc.mapIndexed{idx, value->
                    if(idx==2)value-a[1].toInt() else value
                }
                else -> acc
            }

        }.reduceIndexed{index, acc, i -> if (index!=2) acc*i else acc }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

