package service

import entity.*
import java.util.Stack
/**
 * The RootService class is responsible for managing the overall game logic and state.
 * It initializes and maintains the state of the game, including the game table, players, and card decks.
 */
class RootService: AbstractRefreshingService()  {

    // Represents the game table, including the pyramid, draw pile, and reserve stack.
    val table = Table()
    // Holds the current state of the game, including players and the table.
    var currentGame = GameState( table, Player(""), Player(""))
    // Player service to handle player-specific actions.
    val playerService: PlayerService = PlayerService(this)

    /**
     * Creates a standard deck of 52 playing cards.
     * @return A stack of cards representing a standard deck.
     */
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

    /**
     * Organizes cards into a pyramid structure for the game.
     * @param cards The list of cards to be arranged in the pyramid.
     * @return A MutableList representing the pyramid structure of cards.
     */
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

    /**
     * Starts a new game with the given player names.
     * Initializes the game state including the pyramid, draw pile, reserve stack, and player information.
     * @param aName The name of the first player.
     * @param bName The name of the second player.
     */
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
        val table = Table(reserveStack, drawPile, pyramid)
        val gameState = GameState(table, playerA, playerB)
        this.currentGame = gameState

        // Call refresh method
        onAllRefreshables {
            onGameStart(playerA.name, playerB.name, pyramid, drawPile)
        }
    }


    /**
     * Entry point for starting the game with player names.
     * Invokes the startGame method and checks if the game is finished after setup.
     * @param player1Name The name of the first player.
     * @param player2Name The name of the second player.
     */
    fun rootService(player1Name: String, player2Name: String) {
        startGame(player1Name, player2Name)
        gameFinished()
    }

     /**
     * Checks if the game has finished, either by players passing consecutively or the pyramid being empty.
     * @return Boolean indicating if the game has finished.
     */
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