package service

import entity.*
import java.util.Stack
import kotlin.random.Random

class RootService(private val refreshingService: AbstractRefreshingService) {
    lateinit var currentGame: GameState

    //Creates a MutableList of 52 Cards
    private fun createStandardDeck(): MutableList<Card> {
        return CardSuit.values().flatMap { suit ->
            (1..13).map { value ->
                Card(suit, value)
            }
        }.toMutableList()
    }

    //Transfers the "numberOfCards" Cards to the List
    private fun transferCardsToList(cards: MutableList<Card>, numberOfCards: Int): Stack<Card> {
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
    private fun transferCardsToPyramid(cards: MutableList<Card>, pyramid: MutableList<MutableList<Card?>>) {
        if (cards.size < 28) {
            throw IllegalArgumentException("Not enough cards to fill the pyramid.")
        }

        var cardIndex = 0
        for (level in 1..7) {
            val levelCards = mutableListOf<Card?>()
            for (cardInLevel in 1..level) {
                levelCards.add(cards[cardIndex++])
            }
            if (pyramid.size > level - 1) {
                pyramid[level - 1] = levelCards  // Replace the existing row
            } else {
                pyramid.add(levelCards)  // Add a new row if it does not exist
            }
        }
    }

    //Initializes Stacks, Table and Players
    fun startGame(aName: String, bName : String) : Unit {

        var cards: MutableList<Card> = createStandardDeck().shuffled().toMutableList()
        var reserveStack: Stack<Card> = Stack()
        var drawPile = transferCardsToList(cards, 24)
        val table = Table(reserveStack, drawPile)
        val playerA = Player(aName)
        val playerB = Player(bName)
        transferCardsToPyramid(cards, table.pyramid)
        currentGame = GameState(table, playerA, playerB)
        refreshingService.onAllRefreshables {
            onGameStart(playerA.name,
            playerB.name,
            currentGame.table.pyramid,
                currentGame.table.drawPile)
        }
    }

    //Requires player names as inputs and starts the game
    fun rootService(player1Name: String, player2Name: String) {

        val standardDeck = createStandardDeck() // Function to create 52 standard playing cards
        startGame(player1Name, player2Name)
    }

    //If 2 subsequent pass occurs or pyramid is completely empty game ends
    fun gameFinished() : Boolean {

        if (currentGame.playerA.score > currentGame.playerB.score)
            println("${currentGame.playerA.name} has won")
        else if
            (currentGame.playerA.score < currentGame.playerB.score) println("${currentGame.playerA.name} has won")
        else println("It's a draw ¯\\_(ツ)_/¯")

        refreshingService.onAllRefreshables {
            onGameFinished(
                currentGame.playerA.score,
                currentGame.playerB.score)
        }

        return currentGame.sitOutCount == 2 ||
                currentGame.table.pyramid.all { row ->
                    row.all { it == null }
                }
    }
}