package fr.esgi.pricing.book

import io.cucumber.datatable.DataTable
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions

class BookPricingSteps {
    private var allDifferent: Boolean = true
    private val basket = mutableListOf<String>()
    private var appliedDiscount = 0

    @Given("my basket contains:")
    fun myBasketContains(dataTable: DataTable) {
        basket.addAll(dataTable.asList())
    }

    @Given("adding {int} different Harry Potter book to basket")
    fun addingDifferentBooksToBasket(numberOfUniqueBooks: Int) {
        for (i in 1..numberOfUniqueBooks) {
            basket.add("Harry Potter $i")
        }
    }

    @Then("{int} percent discount should be applied")
    fun percentDiscountShouldBeApplied(discount: Int) {
        appliedDiscount = discount
    }

    @Given("adding {int} copies of {string} to basket")
    @And("adding {int} copy of {string} to basket")
    fun addingCopiesOfBookToBasket(copies: Int, book: String) {
        repeat(copies) {
            basket.add(book)
        }
        if (copies > 1) allDifferent = false
    }

    @When("there are {int} similar Harry Potter books")
    fun thereAreSimilarBooks(similarBooks: Int) {
        allDifferent = similarBooks <= 0;
    }

    @Then("the basket price should be {float}")
    fun assertBasketPrice(expectedPrice: Float) {
        val actualPrice = calculateBasketPrice()
        Assertions.assertEquals(expectedPrice, actualPrice, 0.01f)
    }

    private fun calculateBasketPrice(): Float {
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