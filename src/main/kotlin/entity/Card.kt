package entity

data class Card(
    val value: Int,
    var visible: Boolean,
    var suit: CardSuit? = null
)