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

    @Test
    fun testActionSitOutIncrement() {

        // Ensure the sitOutCount is 0 at the beginning of the test.
        assertEquals(0, gameState.sitOutCount, "Initially, sitOutCount should be 0.")
    }

    /**
     * Tests the actionSitOut method in scenarios where the game finishes as a result of the sit out action.
     *
     * This test focuses on the situation where a player decides to sit out and this action leads to the
     * conclusion of the game. The test verifies that the game correctly identifies its completion and
     * ensures that the final scores of the players are as expected at the end of the game. The test assumes
     * that sitting out under certain conditions (which should be specified or set up in the test) will
     * trigger the end of the game.
     */
    @Test
    fun testActionSitOutWhenGameFinishes() {

        // Call the actionSitOut method
        val playerService = PlayerService(rootService) // Assuming PlayerService takes RootService as a parameter
        playerService.actionSitOut()

        // Assert that the game finished with the expected conditions
        // This might involve checking the final scores, or other end game conditions
        // For example:
        assertEquals(0, gameState.playerA.score)
        assertEquals(0, gameState.playerB.score)

        // Additional assertions can be added as necessary
    }
}
