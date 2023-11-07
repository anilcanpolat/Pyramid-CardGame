package service

import entity.*
import java.util.Stack
import kotlin.random.Random

class RootService {

    //Creates a MutableList of 52 Cards
    private fun createStandardDeck(): MutableList<Card> {
        return CardSuit.values().flatMap { suit ->
            (1..13).map { value ->
                Card(suit, value)
            }
        }.toMutableList()
    }

    //Transfers the "numberOfCards" Cards to the List
    fun transferCardsToList(cards: MutableList<Card>, numberOfCards: Int): Stack<Card> {
        val cardStack = Stack<Card>()

        // Check if the list has enough cards to transfer
        if (cards.size < numberOfCards) {
            throw IllegalArgumentException(
                "The list does not contain enough cards to transfer.")
        }

        // Transfer the specified number of cards from the list to the stack
        for (index in 0 until numberOfCards) {
            cardStack.push(cards[index])
        }
        return cardStack
    }

    //Transfers fix numbers of Cards(28) to pyramid as nested MutableList of MutableList contains Cards
    fun transferCardsToPyramid(cards: MutableList<Card>, pyramid: MutableList<MutableList<Card?>>) {
        if (cards.size < 28) {
            throw IllegalArgumentException("Not enough cards to fill the pyramid.")
        }

        var cardIndex = 0
        for (level in 1..7) {
            val levelCards = mutableListOf<Card?>()
            for (cardInLevel in 1..level) {
                levelCards.add(cards[cardIndex++])
            }
            pyramid.add(levelCards)
        }
    }

    //Initializes Stacks, Table and Players
    fun startGame(aName: String, bName : String) : Unit {

        var reserveStack: Stack<Card> = Stack()
        var drawPile: Stack<Card> = Stack()
        val table = Table(reserveStack, drawPile)
        val playerA = Player(aName)
        val playerB = Player(bName)

        var cards: MutableList<Card> = createStandardDeck().shuffled().toMutableList()

        drawPile = transferCardsToList(cards, 24)
        transferCardsToPyramid(cards, table.pyramid)
        val currentGame = GameState(table, playerA, playerB)
    }

    //Requires player names as inputs and starts the game
    fun rootService() {

        val standardDeck = createStandardDeck() // Function to create 52 standard playing cards
        print("Enter Player1 name: ")
        val player1Name = readLine()

        print("Enter Player2 name: ")
        val player2Name = readLine()

        if(player1Name != null && player2Name != null)
            startGame(player1Name, player2Name)
    }
}