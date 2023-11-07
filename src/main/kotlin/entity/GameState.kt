package entity
import kotlin.random.Random

/**
 * Data class defines the values table, PlayerA, PlayerB
 * and variable currentPlayer as it changes periodically
 */
data class GameState(
    val table: Table,
    val playerA: Player,
    val playerB: Player,
    var currentPlayer: Player = if (Random.nextBoolean()) playerA else playerB
) {
    var sitOutCount: Int = 0 // Counts how many consecutive times players have passed

    init {
        require(sitOutCount < 3 && sitOutCount >= 0) { "Value must be between 0 and 2 (inclusive)" }
    }

    fun switchCurrentPlayer() {
        currentPlayer = if (currentPlayer == playerA) playerB else playerA
    }
}
