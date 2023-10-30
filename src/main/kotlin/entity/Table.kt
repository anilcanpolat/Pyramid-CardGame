package entity
import java.util.Stack

data class Table(
    val reserveStack: Stack<Card> = Stack(),
    val drawPile: Stack<Card> = Stack(),
    val pyramid: Array<Card?> = arrayOfNulls(28)
)