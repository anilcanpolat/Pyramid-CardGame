package entity
import java.util.Stack

/**
 * Table has discrete stacks called reserveStack
 * and drawPile additionally a pyramid that game is played on
 */
data class Table(
    val reserveStack: Stack<Card> = Stack(),
    val drawPile: Stack<Card> = Stack(),
    val pyramid: MutableList<MutableList<Card?>> = MutableList(7) {
        row -> MutableList(row + 1) { null } }
)