package entity

import java.lang.IllegalArgumentException
import kotlin.test.*

class PlayerTest {

    companion object {
        public val testPlayer1 = Player("Rick Sanchez", 137)
    }
    @Test
    fun playerTest() {

        //Invalid input test
        assertFailsWith<IllegalArgumentException>(message = "Score must be greater or equal to 0", block = { Player("Morty Smith", -131)})

        // Test the player's properties
        assertEquals("Rick Sanchez", testPlayer1.name, "It should have been Rick Sanchez")
        assertEquals(137, testPlayer1.score)
    }
}