package entity
import java.util.Stack

/**
 *Table has discrete stacks called reserveStack
 * and drawPile additionally a pyramid that game is played on
 */
data class Table(
    val reserveStack: Stack<Card> = Stack(),
    val drawPile: Stack<Card> = Stack(),
    val pyramid: Array<Card?> = arrayOfNulls(28)
)