package entity

class Table(
    val reserveStack: Stack<Card> = Stack(),
    val drawPile: Stack<Card> = Stack(),
    val pyramid: MutableList<Card> = mutableListOf()
)
class Stack<T> {
    private val items: MutableList<T> = mutableListOf()

    fun push(item: T) {
        items.add(item)
    }

    fun pop(): T? {
        if (isEmpty()) {
            return null
        }
        return items.removeAt(items.size - 1)
    }

    fun peek(): T? {
        return items.lastOrNull()
    }

    fun isEmpty(): Boolean {
        return items.isEmpty()
    }
}