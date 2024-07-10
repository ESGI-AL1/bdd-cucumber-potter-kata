class BookPricingCalculator {
    
     fun calculateBasketPrice(basket: MutableList<String>, allDifferent: Boolean): Float {
        val bookCounts = basket.groupingBy { it }.eachCount()

        fun calculateSetPrice(size: Int): Float = when (size) {
            0 -> 0f
            1 -> 8f
            2 -> 2 * 8f * 0.95f
            3 -> 3 * 8f * 0.90f
            4 -> 4 * 8f * 0.80f
            5 -> 5 * 8f * 0.75f
            else -> throw IllegalArgumentException()
        }

        fun getOptimalPricing(remainingBooks: Map<String, Int>): Float {
            if (remainingBooks.isEmpty()) return 0f

            return if (allDifferent) {
                calculateSetPrice(remainingBooks.size)
            } else {
                (1..minOf(5, remainingBooks.size)).minOf { setSize ->
                    val set = remainingBooks.entries.sortedByDescending { it.value }.take(setSize)
                    val newRemaining = remainingBooks.mapValues { (book, count) ->
                        count - if (set.any { it.key == book }) 1 else 0
                    }.filter { it.value > 0 }

                    calculateSetPrice(setSize) + getOptimalPricing(newRemaining)
                }
            }
        }
        return getOptimalPricing(bookCounts)
    }

}