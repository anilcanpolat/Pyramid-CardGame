package entity

/**
 *Data class is to declare the attributes of card,
 *such as suit, value, visible
 */
data class Card(
    val suit: CardSuit,
    val value: CardValue,  // 2 to 10 for numbers, 11 for Jack, 12 for Queen, 13 for King, 1 for Ace
    var visible: Boolean = false
)