package service

import kotlin.test.*

/**
 * Test suite for the RootService's startGame functionality.
 */
class StartGameTest {

    private lateinit var rootService: RootService

    /**
     * Sets up the test environment before each test, initializing the RootService and starting a new game.
     */
    @BeforeTest
    fun setUp() {
        rootService = RootService()
        // Start the game with two players to set up the currentGame state.
        rootService.startGame("Rick Sanchez", "Morty Smith")
    }

    /**
     * Tests the initial game state after starting a new game to ensure that players,
     * draw pile, and pyramid are correctly initialized.
     */
    @Test
    fun testStartGame() {
        // Verify that the players are initialized correctly with the given names.
        assertEquals("Rick Sanchez", rootService.currentGame.playerA.name,
            "The playerA's name should be 'Rick Sanchez'.")
        assertEquals("Morty Smith", rootService.currentGame.playerB.name,
            "The playerB's name should be 'Morty Smith'.")

        // Verify the draw pile size to ensure it has been properly initialized with 24 cards.
        assertEquals(24, rootService.currentGame.table.drawPile.size,
            "The draw pile should contain 24 cards.")

        // Verify the pyramid structure is correctly populated with 7 rows according to game rules.
        assertEquals(7, rootService.currentGame.table.pyramid.size,
            "The pyramid should have 7 rows.")
        rootService.currentGame.table.pyramid.forEachIndexed { index, row ->
            // Each row in the pyramid should have a number of cards equal to its 1-based index.
            assertEquals(index + 1, row.size,
                "Row ${index + 1} in the pyramid should have ${index + 1} cards.")
            // Ensure that each row in the pyramid is not null, indicating it has been populated with cards.
            assertNotNull(rootService.currentGame.table.pyramid[index],
                "Row ${index + 1} in the pyramid should not be null.")
        }
    }
}
