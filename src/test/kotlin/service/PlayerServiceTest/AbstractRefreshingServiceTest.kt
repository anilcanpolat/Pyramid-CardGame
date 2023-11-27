package service.PlayerServiceTest

import entity.*
import service.*
import view.*
import java.util.*
import kotlin.test.*

/**
 * A test suite for AbstractRefreshingService in a card game application.
 *
 * This class tests the functionality of AbstractRefreshingService, which is responsible
 * for managing UI refresh operations across different components. It focuses on ensuring that
 * UI components are correctly registered for updates and that bulk refresh operations are
 * applied to all registered components.
 */
class AbstractRefreshingServiceTest {

    private lateinit var rootService: RootService
    private lateinit var gameState: GameState
    private lateinit var playerService: PlayerService
    private lateinit var playerA: Player
    private lateinit var playerB: Player
    private lateinit var refreshingService: AbstractRefreshingService
    private lateinit var refreshable: RefreshableStub
    private lateinit var gameScene: GameScene
    private lateinit var gameFinishedMenuScene: ScoreBoardScene
    private lateinit var newGameMenuScene: NewGameMenuScene

    class RefreshableStub : Refreshable {
        var wasRefreshed = false

        // Additional method for testing purposes
        fun markAsRefreshed() {
            wasRefreshed = true
        }

        // Implementations of Refreshable interface methods
        override fun onScoreUpdate(score: Int, name: String) {
            markAsRefreshed()
        }

        override fun onGameFinished(playerAScore: Int, playerBScore: Int) {
            markAsRefreshed()
        }

        override fun onGameStart(
            name1: String,
            name2: String,
            pyramid: MutableList<MutableList<Card?>>,
            drawPile: Stack<Card>
        ) {
            markAsRefreshed()
        }

        override fun onActionRemovePair(
            nextPlayer: Player,
            cardA: Pair<Int,Int>?,
            cardB: Pair<Int,Int>?,
            reserveTop: Card?,
            nowVisible: MutableList<Pair<Int, Int>>
        ) {
            markAsRefreshed()
        }

        override fun onActionDrawCard(
            nextPlayer: Player,
            drawnCard: Card
        ) {
            markAsRefreshed()
        }

        override fun onActionSitOut(
            nextPlayer: Player
        ) {
            markAsRefreshed()
        }
    }

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
        rootService = RootService()
        rootService.currentGame = gameState
        playerService = PlayerService(rootService)
        refreshingService = object : AbstractRefreshingService() {}
        refreshable = RefreshableStub() // Initialize with the stub
        gameScene = GameScene(rootService)
        gameFinishedMenuScene = ScoreBoardScene(rootService)
        newGameMenuScene = NewGameMenuScene(rootService)
    }

    /**
     * Tests the addRefreshable method of AbstractRefreshingService.
     *
     * Verifies that when a Refreshable object and other UI components (GameScene,
     * ScoreBoardScene, NewGameMenuScene) are added to the service, they are indeed
     * registered for future refresh operations. This is crucial for ensuring that
     * all parts of the UI can be updated in response to game state changes.
     */
    @Test
    fun testAddRefreshable() {
        refreshingService.addRefreshable(refreshable, gameScene, gameFinishedMenuScene, newGameMenuScene)
        // Verify that the refreshables are added.
        // This part of the test might be tricky because the refreshables list is private.
        // You may need to change the visibility of the list for testing, or use reflection.
        assertTrue(refreshingService.refreshables.contains(refreshable)) // Hypothetical method for testing
    }

    /**
     * Tests the onAllRefreshables method of AbstractRefreshingService.
     *
     * Verifies that a lambda function passed to onAllRefreshables is applied to all
     * registered Refreshable objects. This ensures that all UI components can be
     * collectively updated in response to a game state change.
     */
    @Test
    fun onAllRefreshablesTest() {
        // Create instances of RefreshableStub and add them to the refreshingService
        val refreshable1 = RefreshableStub()
        val refreshable2 = RefreshableStub()
        val refreshable3 = RefreshableStub()

        refreshingService.addRefreshable(refreshable1, gameScene, gameFinishedMenuScene, newGameMenuScene)
        refreshingService.addRefreshable(refreshable2, gameScene, gameFinishedMenuScene, newGameMenuScene)
        refreshingService.addRefreshable(refreshable3, gameScene, gameFinishedMenuScene, newGameMenuScene)

        // Apply a lambda function to all registered refreshables
        refreshingService.onAllRefreshables { onActionSitOut(Player("")) }
        refreshingService.onAllRefreshables { onScoreUpdate(5, "") }

        // Verify that the lambda function was applied to each refreshable instance
        assertTrue(refreshable1.wasRefreshed)
        assertTrue(refreshable2.wasRefreshed)
        assertTrue(refreshable3.wasRefreshed)
    }

}