package view
import service.*
import entity.*
import java.util.Stack

interface Refreshable {

    /**
     * Called when there is an update to a player's score.
     * Implementing classes should use this method to refresh UI components or game state that depend on player scores.
     *
     * @param score The updated score of the player.
     * @param name The name of the player whose score is updated.
     */
    fun onScoreUpdate(score: Int, name: String): Unit {}
    /**
     * Called when a game round finishes.
     * Implementing classes should use this method to handle the end of a game, such as updating UI to show final scores.
     *
     * @param playerAScore Final score of player A.
     * @param playerBScore Final score of player B.
     */
    fun onGameFinished(playerAScore: Int,
                       playerBScore: Int): Unit {}
    /**
     * Called at the start of a new game.
     * Implementing classes should use this method to initialize or reset components for a new game session.
     *
     * @param name1 Name of the first player.
     * @param name2 Name of the second player.
     * @param pyramid Initial layout of the pyramid.
     * @param drawPile Initial stack of cards for drawing.
     */
    fun onGameStart(
        name1: String,
        name2: String,
        pyramid: MutableList<MutableList<Card?>>,
        drawPile: Stack<Card>
    ): Unit {}
    /**
     * Called when a pair of cards is removed from the game.
     * Implementing classes should use this method to update the game state and UI following the removal of a card pair.
     *
     * @param nextPlayer The player who will take the next turn.
     * @param cardA The position of the first card in the pair, if any.
     * @param cardB The position of the second card in the pair, if any.
     * @param reserveTop The top card of the reserve pile, if any.
     * @param nowVisible A list of newly visible card positions.
     */
    fun onActionRemovePair(
        nextPlayer: Player,
        cardA: Pair<Int,Int>?,
        cardB: Pair<Int,Int>?,
        reserveTop: Card?,
        nowVisible: MutableList<Pair<Int, Int>>
    ): Unit {}
    /**
     * Called when a player draws a card.
     * Implementing classes should use this method to handle the game state and UI changes resulting from a card draw.
     *
     * @param nextPlayer The player who will take the next turn.
     * @param drawnCard The card that was drawn.
     */
    fun onActionDrawCard(
        nextPlayer: Player,
        drawnCard: Card
    ): Unit {}
    /**
     * Called when a player chooses to sit out a turn.
     * Implementing classes should use this method to update the game state and UI to reflect the change in player turns.
     *
     * @param nextPlayer The player who will take the next turn.
     */
    fun onActionSitOut(
        nextPlayer: Player
    ): Unit {}

}