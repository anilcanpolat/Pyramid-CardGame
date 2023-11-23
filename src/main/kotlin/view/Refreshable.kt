package view

import entity.*
import java.util.Stack

/**
 * An interface representing a UI component or scene that can be refreshed or updated.
 * Classes implementing this interface should define how they update their state or appearance
 * in response to changes in the game's data or state.
 *
 * The Refreshable interface is typically used in game applications where multiple parts
 * of the UI need to be updated synchronously to reflect the current game state. For example,
 * a game board, score display, or player inventory UI could implement this interface to ensure
 * they reflect the latest game data.
 *
 * Methods that should be implemented by classes implementing this interface might include
 * updating visual elements, resetting state, or responding to game events.
 */
interface Refreshable {

    /**
     * Called when there is an update to a player's score.
     * Implementing classes should use this method to refresh UI components or game state that depend on player scores.
     *
     * @param score The updated score of the player.
     * @param name The name of the player whose score is updated.
     */
    fun onScoreUpdate(score: Int, name: String) {}
    /**
     * Called when a game round finishes.
     * Implementing classes should use this method to handle the end of a game, such as updating UI to show final scores.
     *
     * @param playerAScore Final score of player A.
     * @param playerBScore Final score of player B.
     */
    fun onGameFinished(playerAScore: Int,
                       playerBScore: Int) {}
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
    ) {}
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
    ) {}
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
    ) {}
    /**
     * Called when a player chooses to sit out a turn.
     * Implementing classes should use this method to update the game state and UI to reflect the change in player turns.
     *
     * @param nextPlayer The player who will take the next turn.
     */
    fun onActionSitOut(
        nextPlayer: Player
    ) {}

}