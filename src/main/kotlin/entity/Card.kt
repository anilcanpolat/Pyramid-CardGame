package entity

data class Card(
    val suit: CardSuit,
    val value: Int,  // 2 to 10 for numbers, 11 for Jack, 12 for Queen, 13 for King, 1 for Ace
    var visible: Boolean = false
) {
    init {
        require(value in 1..13) { "Value must be between 1 and 13 (inclusive)" }
    }
}