package service

import entity.*
import java.util.Stack

class RootService: AbstractRefreshingService()  {

    val table = Table()
    var currentGame = GameState( table, Player(""), Player(""))
    val playerService: PlayerService = PlayerService(this)

    //Creates a MutableList of 52 Cards
    private fun createStandardDeck(): Stack<Card> {
        val standardDeck = Stack<Card>()
        for (suit in CardSuit.values()) {
            for (value in CardValue.values()) {
                val card = Card(suit, value)
                standardDeck.push(card)
            }
        }
        return standardDeck
    }

    private fun organizeCardsInPyramid(cards: List<Card>): MutableList<MutableList<Card?>> {
        val pyramid = MutableList(7) { MutableList<Card?>(7) { null } }
        val visibleIndices = setOf(0,1,2,3,5,6,9,10,14,15,20,21,27)
        var index = 0
        for (row in 0 until 7) {
            for (column in 0 until row + 1) {
                if (index < cards.size) {
                    val card = cards[index]
                    pyramid[row][column] = card
                    // Make the outer cards visible
                    if (index in visibleIndices){
                        card.visible = true
                    }
                    index++
                }
            }
        }
        return pyramid
    }

    // Initializes Stacks, Table, and Players
    fun startGame(aName: String, bName: String) {
        val cards: MutableList<Card> = createStandardDeck().shuffled().toMutableList()
        
        val pyramid = organizeCardsInPyramid(cards)

        // Initialize the reserve stack and draw pile
        val reserveStack = Stack<Card>()

        val drawPile: Stack<Card> = Stack()

        for(card in 28..51) {
            drawPile.push(cards[card])
        }

        // Create players
        val playerA = Player(aName)
        val playerB = Player(bName)
        // Initialize the game state
        val table = Table(reserveStack, drawPile)
        val gameState = GameState(table, playerA, playerB)
        this.currentGame = gameState

        // Call refresh method
        onAllRefreshables {
            onGameStart(playerA.name, playerB.name, pyramid, gameState.table.drawPile)
        }
    }


    //Requires player names as inputs and starts the game
    fun rootService(player1Name: String, player2Name: String) {
        startGame(player1Name, player2Name)
    }

    //If 2 subsequent pass occurs or pyramid is completely empty game ends
    fun gameFinished() : Boolean {

        val status: Boolean = currentGame.sitOutCount == 2 ||
                currentGame.table.pyramid.all { row ->
                    row.all { it == null }
                }
        onAllRefreshables {
        onGameFinished(currentGame.playerA.score, currentGame.playerB.score) }
        return status
    }
}