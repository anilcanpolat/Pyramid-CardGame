package entity
import kotlin.test.*

class GameStateTest {
    private var testInt1 = 0
    private val testGameState: GameState = GameState(TableTest.testTable1,PlayerTest.testPlayer1, playerB = Player("Morty Smith", 131))

    @Test
    fun gameStateTest() {

        //Testing if switching from A to B
        var testCurrentPlayer: Player = testGameState.currentPlayer
        testGameState.switchCurrentPlayer()
        assertNotEquals(testCurrentPlayer, testGameState.currentPlayer)

        //Testing vice versa
        testCurrentPlayer = testGameState.currentPlayer
        testGameState.switchCurrentPlayer()
        assertNotEquals(testCurrentPlayer, testGameState.currentPlayer)

        //Testing if sitOutCount increments
        testInt1 += 1
        assertEquals(testInt1,1)
        //Testing if sitOutCount decrements
        testInt1 -=1
        assertEquals(testInt1,0)
    }
}