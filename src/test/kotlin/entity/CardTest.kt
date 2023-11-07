package entity
import java.lang.IllegalArgumentException
import kotlin.test.*

/**
 *Test class' main purpose is to test different realizations.
 *Such as if the cards value, card suit and if the card is visible
 */
class CardTest {

    companion object {
        val testCard1 = Card(CardSuit.HEARTS, 10)
        val testCard2 = Card(CardSuit.CLUBS, 5, true)
    }

    /**
     * Function serves as testing the above given tasks.
     */
    @Test
    fun cardTests() {

        assertFailsWith<IllegalArgumentException>(
            message = "Value must be between 1 and 13 (inclusive)",
            block = { Card(CardSuit.DIAMONDS, 15, false) }
        )

        // Test the card's properties
        assertEquals(10, testCard1.value, "It should have been 10")
        assertEquals(CardSuit.HEARTS, testCard1.suit,"It should have been HEARTS")
        assertFalse(testCard1.visible)

        // Test the card's properties
        assertEquals(5, testCard2.value,"It should have been 5")
        assertEquals(CardSuit.CLUBS, testCard2.suit, "It should have been CLUBS")
        assertTrue(testCard2.visible)
    }
}