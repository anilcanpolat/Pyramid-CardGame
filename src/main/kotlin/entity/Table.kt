package entity

data class Table(
    val reserveStack: Stack<Card> = Stack(),
    val drawPile: Stack<Card> = Stack(),
    val pyramid: MutableList<Card> = mutableListOf()
)