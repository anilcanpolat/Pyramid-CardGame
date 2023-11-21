package service.RootServiceTest

import entity.Card
import entity.GameState
import entity.Player
import entity.Table
import service.AbstractRefreshingService
import service.RootService
import view.Refreshable
import java.util.*
import kotlin.test.*

/**
 * Test suite for testing the RootService class which is responsible for starting a new game.
 */
class RootServiceTest {

    private lateinit var rootService: RootService
    /**
     * Prepares the testing environment by creating an instance of RootService before each test.
     */
    @BeforeTest
    fun setUp() {
        rootService = RootService()
    }

    /**
     * Verifies that the startGame method initializes a game with the correct player names.
     */
    @Test
    fun rootServiceNameMatch() {
        // Calling startGame to initialize the game with two player names.
        rootService.startGame("Rick Sanchez", "Morty Smith")

        // Verifying that the names of the players in the current game match those provided.
        assertEquals("Rick Sanchez", rootService.currentGame.playerA.name,
            "The name of playerA should match the name provided at the start of the game.")
        assertEquals("Morty Smith", rootService.currentGame.playerB.name,
            "The name of playerB should match the name provided at the start of the game.")
    }
}
