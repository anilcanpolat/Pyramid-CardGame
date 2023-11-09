package service.PlayerServiceTest

import entity.*
import service.PlayerService
import java.util.*
import kotlin.test.*

/**
 * Test suite for the PlayerService's actionDrawCard method.
 */
class ActionDrawCardTest {

    private lateinit var gameState: GameState
    private lateinit var playerService: PlayerService
    private lateinit var playerA: Player
    private lateinit var playerB: Player

    /**
     * Sets up the test environment with default game state, table, and players.
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
     * Tests the actionDrawCard method for the following scenarios:
     * - Throws an IllegalStateException when the draw pile is empty.
     * - A card is drawn correctly from the draw pile when it's not empty.
     */
    @Test
    fun testActionDrawCard() {
        // Ensure the draw pile is empty and expect an exception when trying to draw a card
        gameState.table.drawPile.clear()

        assertFailsWith<IllegalStateException> {
            playerService.actionDrawCard()
        }

        // Add a card to the draw pile and test the draw card functionality
        val cardA = Card(CardSuit.HEARTS, 7)
        gameState.table.drawPile.push(cardA)

        assertEquals(1, gameState.table.drawPile.size)

        // Execute the action and verify the draw pile is now empty and the card is in the reserve stack
        playerService.actionDrawCard()

        assertEquals(0, gameState.table.drawPile.size)
        assertEquals(cardA, gameState.table.reserveStack.peek())
    }
}
