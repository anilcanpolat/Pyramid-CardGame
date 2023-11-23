package entity
import java.util.*
import kotlin.test.*

/**
 *TableTest is to test if different Arrays initiate the Pyramid and
 *if the empty stacks and empty Stacks initiate Pyramid
 */
class TableTest {

    companion object {
        //Constructing the testStacks for reserveStack and drawPile
        val testStack1: Stack<Card> = Stack()
        val testStack2: Stack<Card> = Stack()

        //Constructing the testPyramids
        var testPyramid: MutableList<MutableList<Card?>> = (1..7).map { size ->
            MutableList(size) { null as Card? }
        }.toMutableList()

        //Constructing the testTables
        var testTable1: Table
        init {
            //Pushing the cards from CardTest to testStacks
            testStack1.push(CardTest.testCard1)
            testStack2.push(CardTest.testCard2)

            //initiating the testSample1 as modified and non-empty stack and array
            testPyramid[0][0] = Card(CardSuit.DIAMONDS, CardValue.FOUR, false)
            testPyramid[1][0] = Card(CardSuit.CLUBS, CardValue.QUEEN, true)

            testTable1 = Table(testStack1, testStack2, testPyramid)
            }
    }

    /**
     *
     * Function serves as testing the above given tasks.
     */
    @Test
    fun tableTests() {

        // Test the card's properties
        assertEquals(testStack1, testTable1.reserveStack, "Reserve stack is not equal")
        assertEquals(testStack2, testTable1.drawPile,"Draw pile is not equal")

        assertEquals(testPyramid[0][0],Card(CardSuit.DIAMONDS, CardValue.FOUR, false))
        assertEquals(testPyramid[1][0],Card(CardSuit.CLUBS, CardValue.QUEEN, true))

    }
}