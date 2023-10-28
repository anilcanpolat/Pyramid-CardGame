package entity

data class GameState(
    var currentPlayer: Player? = null,
    var sitOutCount: Int = 0,
    val table: Table = Table(),
    val playerA: Player = Player(),
    val playerB: Player = Player()
)