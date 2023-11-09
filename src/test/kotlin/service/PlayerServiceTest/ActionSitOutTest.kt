package service

import entity.*
import java.util.*
import kotlin.test.*

/**
 * Test suite for testing the PlayerService's actionSitOut method.
 */
class ActionSitOutTest {

    private lateinit var gameState: GameState
    private lateinit var playerService: PlayerService
    private lateinit var playerA: Player
    private lateinit var playerB: Player

    /**
     * Sets up the test environment before each test. This includes initializing players, game state,
     * and the table with empty card stacks.
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
     * Tests that the actionSitOut method increments the sitOutCount in the game state.
     */
    @Test
    fun testActionSitOut() {
        // Ensure the sitOutCount is 0 at the beginning of the test.
        assertEquals(0, gameState.sitOutCount, "Initially, sitOutCount should be 0.")

        // Perform the action to sit out.
        playerService.actionSitOut()

        // Assert that the sitOutCount has been incremented to 1.
        assertEquals(1, gameState.sitOutCount, "After sitting out, sitOutCount should be 1.")
    }
}
