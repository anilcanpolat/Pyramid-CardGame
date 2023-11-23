package view

import entity.*
import service.*
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color
import java.util.*

/**
 * Constructs a menu scene for displaying the game's result.
 * Initializes UI components to show scores, the game outcome, and provides options to start a new game or quit.
 *
 * @param rootService The root service for accessing game data and controlling game flow.
 */
class GameFinishedMenuScene(private val rootService: RootService) : MenuScene(480, 776), Refreshable {

    /**
     * Label component configuration. The label is set to cover the entire scene's width and height.
     * It is positioned at the top-left corner of the scene (posX = 0, posY = 0) and displays the greeting
     * message with a font size of 20.
     */
    private val headlineLabel = Label(
        width = 300, height = 50, posX = 50, posY = 50,
        text = "Game Over",
        font = Font(size = 22)
    )

    /**
     * A label for displaying Player 2's score.
     * Positioned to show the score clearly after the game concludes.
     */
    private val p2Score = Label(width = 300, height = 35, posX = 50, posY = 125)
    /**
     * A label for displaying Player 1's score.
     * Positioned to show the score clearly after the game concludes.
     */
    private val p1Score = Label(width = 300, height = 35, posX = 50, posY = 160)
    /**
     * A label to display the game result.
     * Indicates whether it's a win for one of the players or a draw.
     */
    private val gameResult = Label(width = 300, height = 35, posX = 50, posY = 195).apply {
    }
    /**
     * A button for quitting the application.
     * Allows the player to exit the game after viewing the results.
     */
    val quitButton = Button(width = 140, height = 35, posX = 50, posY = 265, text = "Quit").apply {
        visual = ColorVisual(Color(224,45,29))
    }
    /**
     * A button to start a new game.
     * Initiates a new game session when clicked.
     */
    val newGameButton = Button(width = 140, height = 35, posX = 210, posY = 265, text = "New Game").apply {
        visual = ColorVisual(Color(58, 224, 29))
    }
    /**
     * Initializes the scene with the defined UI components.
     * Sets up the layout, positioning, and initial properties of each component.
     */
    init {
        opacity = .5
        addComponents(headlineLabel, p1Score, p2Score, gameResult, newGameButton, quitButton)
    }

    /**
     * Helper function to format the score display string for a player.
     *
     * @return A formatted string showing the player's name and score.
     */
    private fun Player.scoreString(): String = "${this.name} scored ${this.score} points."
    /**
     * Helper function to create a string representing the game's outcome.
     * Determines the winner or if the game ended in a draw based on player scores.
     *
     * @return A string indicating the game result.
     */
    private fun GameState.gameResultString(): String {
        val playerAScore = playerA.score
        val playerBScore = playerB.score
        return when {
            playerAScore > playerBScore -> "${playerA.name} wins the game."
            playerAScore < playerBScore  -> "${playerB.name} wins the game."
            else -> "Draw. No winner."
        }
    }

    /**
     * Called when a game finishes.
     * Updates the scene to display each player's score and the overall result of the game.
     *
     * @param playerAScore Final score of player A.
     * @param playerBScore Final score of player B.
     */
    override fun onGameFinished(playerAScore: Int, playerBScore: Int) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        p1Score.text = game.playerA.scoreString()
        p2Score.text = game.playerB.scoreString()
        gameResult.text = game.gameResultString()
    }

}