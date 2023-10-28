package entity
data class GameState(
    var currentPlayer: Player,
    val table: Table,
    val playerA: Player,
    val playerB: Player
) {
    var sitOutCount: Int = 0  // Counts how many consecutive times players have passed

    fun switchCurrentPlayer() {
        currentPlayer = if (currentPlayer == playerA) playerB else playerA
    }

    // Additional functions to manage game state can be added here
}
