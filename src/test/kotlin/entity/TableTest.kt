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
        var testPyramid: Array<Card?> = Array(28) { null }

        //Constructing the testTables
        public var testTable1: Table
        init {
            //Pushing the cards from CardTest to testStacks
            testStack1.push(CardTest.testCard1)
            testStack2.push(CardTest.testCard2)

            //initiating the testSample1 as modified and non-empty stack and array
            testPyramid[0] = Card(CardSuit.DIAMONDS, 4, false)
            testPyramid[1] = Card(CardSuit.CLUBS, 12, true)

            testTable1 = Table(testStack1, testStack2, testPyramid)
            }
    }
    @Test
    fun tableTests() {

        // Test the card's properties
        assertEquals(testStack1, testTable1.reserveStack, "Reserve stack is not equal")
        assertEquals(testStack2, testTable1.drawPile,"Draw pile is not equal")

        assertEquals(testPyramid[0],Card(CardSuit.DIAMONDS, 4, false))
        assertEquals(testPyramid[1],Card(CardSuit.CLUBS, 12, true))
        for(i in 2..27) {
            assertNull(testPyramid[i])
        }
    }
}