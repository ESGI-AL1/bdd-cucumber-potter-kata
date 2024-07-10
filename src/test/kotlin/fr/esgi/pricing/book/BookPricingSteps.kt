package fr.esgi.pricing.book

import BookPricingCalculator
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
    private var calculator = BookPricingCalculator();

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
        val actualPrice = calculator.calculateBasketPrice(basket, allDifferent);
        Assertions.assertEquals(expectedPrice, actualPrice, 0.01f)
    }
}