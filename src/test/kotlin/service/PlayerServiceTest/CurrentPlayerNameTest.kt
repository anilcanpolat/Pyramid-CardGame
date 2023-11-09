package service.PlayerServiceTest

import entity.*
import service.PlayerService
import java.util.*
import kotlin.test.*

/**
 * Test suite for testing the currentPlayerName method of the PlayerService.
 */
class CurrentPlayerNameTest {

    private lateinit var gameState: GameState
    private lateinit var playerService: PlayerService
    private lateinit var playerA: Player
    private lateinit var playerB: Player

    /**
     * Sets up the test environment before each test, initializing players with names and an empty game state.
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
        gameState.currentPlayer = playerA
    }

    /**
     * Tests that the currentPlayerName method returns the correct name of the current player.
     */
    @Test
    fun testCurrentPlayerName() {
        // Initially, playerA is the current player, so we check that their name matches.
        assertEquals("Rick Sanchez", playerService.currentPlayerName(),
            "The current player's name should be Rick Sanchez.")
        gameState.switchCurrentPlayer()
        // Assert that playerB's name is not mistakenly returned as the current player's name.
        assertEquals("Morty Smith", playerService.currentPlayerName(),
            "The current player's name should not be Jerry Smith.")
    }
}