package entity

data class Player(
    var name: String = "",
    var score: Int = 0
) {
    init {
        require(score >= 0) { "Score must be greater or equal to 0" }
    }
}