package entity
import kotlin.test.*

/**
 * GameStateTest is build around if the currentPlayer changes
 * every round and if the sitOutCount increments and decrements
 */
class GameStateTest {
    private var testInt1 = 0
    private val testGameState: GameState = GameState(
        table = TableTest.testTable1,
        playerA = PlayerTest.testPlayer1,
        playerB = Player("Morty Smith")
    )

    /**
     * Function serves as testing the above given tasks.
     */
    @Test
    fun gameStateTests() {

        testGameState.sitOutCount = testInt1
        //Testing if switching from A to B
        var testCurrentPlayer: Player = testGameState.currentPlayer
        testGameState.switchCurrentPlayer()
        assertNotEquals(testCurrentPlayer, testGameState.currentPlayer)

        //Testing vice versa
        testCurrentPlayer = testGameState.currentPlayer
        testGameState.switchCurrentPlayer()
        assertNotEquals(testCurrentPlayer, testGameState.currentPlayer)

        //Testing if sitOutCount increments
        testGameState.sitOutCount += 1
        assertEquals(testGameState.sitOutCount,1)
        //Testing if sitOutCount decrements
        testGameState.sitOutCount -=1
        assertEquals(testGameState.sitOutCount,0)
    }
}