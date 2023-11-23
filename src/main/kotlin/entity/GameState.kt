package entity
import kotlin.random.Random

/**
 * Represents the state of a game, including the game table, the two players, and the current player.
 *
 * This class is central to managing the state and progress of the game. It tracks which player's turn it is,
 * and how many consecutive times players have chosen to pass their turn (tracked by `sitOutCount`).
 *
 * @property table The game table, encapsulating elements like the card pyramid and decks.
 * @property playerA The first player in the game.
 * @property playerB The second player in the game.
 */
data class GameState(
    val table: Table,
    val playerA: Player,
    val playerB: Player,

) {
    var currentPlayer: Player = if (Random.nextBoolean()) playerA else playerB
    var sitOutCount: Int = 0 // Counts how many consecutive times players have passed

    init {
        require(sitOutCount < 3 && sitOutCount >= 0) { "Value must be between 0 and 2 (inclusive)" }
    }

    /**
     * Switches the turn to the other player.
     * This method is called to alternate turns between playerA and playerB.
     */
    fun switchCurrentPlayer() {
        currentPlayer = if (currentPlayer == playerA) playerB else playerA
    }
}
