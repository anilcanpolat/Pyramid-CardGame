package entity

/**
 *Every player has variable names and scores
 *as they can later on the game change
 */
data class Player(
    val name: String = "",
    var score: Int = 0
) {
    init {
        require(score >= 0) { "Score must be greater or equal to 0" }
    }
}