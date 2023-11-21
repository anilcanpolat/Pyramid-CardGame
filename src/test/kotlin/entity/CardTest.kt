package entity
import java.lang.IllegalArgumentException
import kotlin.test.*

/**
 *Test class' main purpose is to test different realizations.
 *Such as if the cards value, card suit and if the card is visible
 */
class CardTest {

    companion object {
        val testCard1 = Card(CardSuit.HEARTS, CardValue.TEN)
        val testCard2 = Card(CardSuit.CLUBS, CardValue.FIVE, true)
    }

    /**
     * Function serves as testing the above given tasks.
     */
    @Test
    fun cardTests() {


        // Test the card's properties
        assertEquals(CardValue.TEN, testCard1.value, "It should have been 10")
        assertEquals(CardSuit.HEARTS, testCard1.suit,"It should have been HEARTS")
        assertFalse(testCard1.visible)

        // Test the card's properties
        assertEquals(CardValue.FIVE, testCard2.value,"It should have been 5")
        assertEquals(CardSuit.CLUBS, testCard2.suit, "It should have been CLUBS")
        assertTrue(testCard2.visible)
    }
}