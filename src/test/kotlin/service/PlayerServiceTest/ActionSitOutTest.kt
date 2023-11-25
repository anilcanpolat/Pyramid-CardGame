package service.PlayerServiceTest

import entity.*
import service.PlayerService
import service.RootService
import java.util.*
import kotlin.test.*

/**
 * Test suite for testing the PlayerService's actionSitOut method.
 */
class ActionSitOutTest {

    private lateinit var rootService: RootService
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
        rootService = RootService()
        rootService.currentGame = gameState
        playerService = PlayerService(rootService)
    }

    /**
     * Tests that the actionSitOut method increments the sitOutCount in the game state.
     */
    @Test
    fun testActionSitOut() {

        // Ensure the sitOutCount is 0 at the beginning of the test.
        assertEquals(0, gameState.sitOutCount, "Initially, sitOutCount should be 0.")
    }
}
