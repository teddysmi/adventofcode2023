fun main() {

    val cardOrder = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2').reversed()
    val part2CardOrder = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J').reversed()
    val cardRank = listOf(listOf(5), listOf(4, 1), listOf(3, 2), listOf(3, 1, 1), listOf(2, 2, 1), listOf(2, 1, 1, 1), listOf(1, 1, 1, 1, 1)).reversed()

    data class CardStringWithBet(val cardString: String, val bet: Long)

    fun CardStringWithBet.foldToMap(): MutableMap<Char, Int> {
        return this.cardString.fold(mutableMapOf()) { acc, next ->
            if (acc.containsKey(next)) {
                acc[next] = (acc[next] ?: 0) + 1
            } else {
                acc[next] = 1
            }
            acc
        }
    }

    fun Map<Char, Int>.rankAfterReplacingJoker(): List<Int> {
        if (!this.containsKey('J')) {
            return this.values.sortedDescending()
        }
        val numberOfJokers = this['J'] ?: 0
        val filteredJokerValues = this.filter { it.key != 'J' }.values.sortedDescending()
        if (filteredJokerValues.isNotEmpty()) return filteredJokerValues.mapIndexed { index, i -> if (index == 0) i + numberOfJokers else i } else return listOf(5)

    }

    val cardComparator = Comparator<CardStringWithBet> { c1, c2 ->
        var difference = 0
        val c1Rank = cardRank.indexOf(c1.foldToMap().values.sortedDescending())
        val c2Rank = cardRank.indexOf(c2.foldToMap().values.sortedDescending())
        if (c1Rank != c2Rank) {
            difference = c1Rank - c2Rank
        } else {
            for ((index, card) in c1.cardString.withIndex()) {
                val c1Order = cardOrder.indexOf(card)
                val c2Order = cardOrder.indexOf(c2.cardString[index])
                if (c1Order != c2Order) {
                    difference = c1Order - c2Order
                    break
                }
            }
        }
        difference
    }

    val part2CardComparator = Comparator<CardStringWithBet> { c1, c2 ->
        var difference = 0
        val c1Rank = cardRank.indexOf(c1.foldToMap().rankAfterReplacingJoker())
        val c2Rank = cardRank.indexOf(c2.foldToMap().rankAfterReplacingJoker())
        if (c1Rank != c2Rank) {
            difference = c1Rank - c2Rank
        } else {
            for ((index, card) in c1.cardString.withIndex()) {
                val c1Order = part2CardOrder.indexOf(card)
                val c2Order = part2CardOrder.indexOf(c2.cardString[index])
                if (c1Order != c2Order) {
                    difference = c1Order - c2Order
                    break
                }
            }
        }
        difference
    }

    fun part1(input: List<String>): Long {
        val cardStringWithBet = input.map {
            val (cards, bet) = it.split(" ")
            CardStringWithBet(cards.trim(), bet.trim().toLong())
        }
        return cardStringWithBet.sortedWith(cardComparator).foldIndexed(0) { index, acc, card ->
            acc + (index + 1) * card.bet
        }
    }

    fun part2(input: List<String>): Long {
        val cardStringWithBet = input.map {
            val (cards, bet) = it.split(" ")
            CardStringWithBet(cards.trim(), bet.trim().toLong())
        }
        val sortedCard = cardStringWithBet.sortedWith(part2CardComparator)
        println(sortedCard.map{it.cardString})
        return sortedCard.foldIndexed(0) { index, acc, card ->
            acc + (index + 1) * card.bet
        }
    }


    val testInput = readInput("Day07_test")
    check(part2(testInput) == 5905L)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))

}