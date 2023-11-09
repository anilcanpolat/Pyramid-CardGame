package entity

import java.lang.IllegalArgumentException
import kotlin.test.*

/**
 * PlayerTest class tests if the invalid initiations fail
 * and if they initiate with valid parameters
 */
class PlayerTest {

    companion object {
        public val testPlayer1 = Player("Rick Sanchez")
    }

    /**
     * Function serves as testing the above given tasks.
     */
    @Test
    fun playerTests() {

        //Invalid input test
        assertFailsWith<IllegalArgumentException>(
            message = "Score must be greater or equal to 0")
        { Player("Morty Smith", -131) }

        // Test the player's properties
        assertEquals("Rick Sanchez", testPlayer1.name, "It should have been Rick Sanchez")
    }
}