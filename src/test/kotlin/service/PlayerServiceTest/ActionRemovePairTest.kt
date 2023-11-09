package service.PlayerServiceTest

import entity.*
import service.*
import kotlin.test.*
import java.util.*

/**
 * Test suite for testing the removal of card pairs from the pyramid or reserve stack in PlayerService.
 */
class ActionRemovePairTest {

    private lateinit var gameState: GameState
    private lateinit var playerService: PlayerService
    private lateinit var playerA: Player
    private lateinit var playerB: Player

    /**
     * Sets up a game state with players and empty card stacks before each test.
     */
    @BeforeTest
    fun setUp() {
        playerA = Player("Rick Sanchez")
        playerB = Player("Morty Smith")
        val reserveStack = Stack<Card>()
        val drawPile = Stack<Card>()
        val table = Table(reserveStack, drawPile)
        gameState = GameState(table, playerA, playerB)
        playerService = PlayerService(gameState)

    }

    /**
     * Tests that a valid pair of cards with the sum of 15 is removed from the pyramid.
     */
    @Test
    fun testValidPairRemovalFromPyramid() {
        // Assuming the pyramid has been setup with the right cards

        val cardA = Card(CardSuit.HEARTS, 7)  // Any value other than Ace (1)
        val cardB = Card(CardSuit.CLUBS, 8)   // Any value that sums with cardA to 15
        gameState.table.pyramid[1][1] = cardA
        gameState.table.pyramid[2][1] = cardB

        playerService.actionRemovePair(cardA, cardB, false)

        /*Requires Refreshables
        assertEquals(initialScore + 2, gameState.currentPlayer.score)
*/
        assertNull(gameState.table.pyramid.find { row -> row.contains(cardA) })
        assertNull(gameState.table.pyramid.find { row -> row.contains(cardB) })
    }

    /**
     * Tests that a valid pair is removed when one card is from the reserve stack.
     */
    @Test
    fun testValidPairRemovalWithOneCardFromReserve() {
        // Setup the reserve with a card and ensure the pyramid has a matching card
        val cardA = Card(CardSuit.HEARTS, 7)
        gameState.table.reserveStack.push(cardA) // Card from reserve
        val cardB = Card(CardSuit.CLUBS, 8)
        gameState.table.pyramid[0][0] = cardB // Card from pyramid
        playerService.actionRemovePair(cardA, cardB, true)

        assertTrue(gameState.table.reserveStack.isEmpty())
        assertNull(gameState.table.pyramid[0][0])
        //Requires Refreshables
        //assertEquals(initialScore+2, gameState.currentPlayer.score)
    }

    /**
     * Tests that an invalid pair removal attempt does not alter the pyramid structure.
     */
    @Test
    fun testInvalidPairRemovalAttempt() {
        // Setup invalid pair

        val cardA = Card(CardSuit.HEARTS, 2)
        val cardB = Card(CardSuit.CLUBS, 2)

        gameState.table.pyramid[0][0] = cardA
        gameState.table.pyramid[1][0] = cardB

        playerService.actionRemovePair(cardA, cardB, false)

        assertTrue(gameState.table.pyramid.any{ row -> row.contains(cardA) })

        val cardC = Card(CardSuit.HEARTS, 7)
        val cardD = Card(CardSuit.CLUBS, 8)

        gameState.table.pyramid[0][0] = cardC
        gameState.table.pyramid[1][0] = cardD

        assertFailsWith<IllegalStateException> {
            playerService.actionRemovePair(cardC, cardD, true)
        }
    }

    /**
     * Tests drawing a card from a non-empty draw pile and verifies the draw pile and reserve stack states.
     */
    @Test
    //check drawing a card with non-empty draw pile
    fun testActionDrawCardWithCardsRemaining() {

        val cardA = Card(CardSuit.HEARTS, 2)
        val cardB = Card(CardSuit.CLUBS, 2)
        gameState.table.drawPile.push(cardA)
        gameState.table.drawPile.push(cardB)

        playerService.actionDrawCard()

        //after drawing card2, card1 should stay
        assertTrue(gameState.table.drawPile.contains(cardA))
        assertFalse(gameState.table.drawPile.contains(cardB))
        assertTrue(gameState.table.drawPile.contains(cardA) || gameState.table.drawPile.contains(cardB))
    }

    /**
     * Tests drawing a card from an empty draw pile and checks the reserve stack is updated correctly.
     */
    @Test
    fun testActionDrawCardWithNoCardsRemaining() {

        gameState.table.drawPile.clear()
        val cardA = Card(CardSuit.HEARTS, 2)
        val cardB = Card(CardSuit.CLUBS, 2)
        gameState.table.drawPile.push(cardA)
        gameState.table.drawPile.push(cardB)

        playerService.actionDrawCard()
        assertEquals(cardB,gameState.table.reserveStack.peek())
        playerService.actionDrawCard()

        // Check that the reserve stack remains empty
        assertTrue(gameState.table.drawPile.isEmpty())
    }

}
