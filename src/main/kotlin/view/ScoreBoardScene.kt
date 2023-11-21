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

    private val p2Score = Label(width = 300, height = 35, posX = 50, posY = 125)

    private val p1Score = Label(width = 300, height = 35, posX = 50, posY = 160)

    private val gameResult = Label(width = 300, height = 35, posX = 50, posY = 195).apply {
    }

    val quitButton = Button(width = 140, height = 35, posX = 50, posY = 265, text = "Quit").apply {
        visual = ColorVisual(Color(224,45,29))
    }

    val newGameButton = Button(width = 140, height = 35, posX = 210, posY = 265, text = "New Game").apply {
        visual = ColorVisual(Color(58, 224, 29))
    }

    init {
        opacity = .5
        addComponents(headlineLabel, p1Score, p2Score, gameResult, newGameButton, quitButton)
    }

    private fun Player.scoreString(): String = "${this.name} scored ${this.score} points."

    private fun GameState.gameResultString(): String {
        val playerAScore = playerA.score
        val playerBScore = playerB.score
        return when {
            playerAScore > playerBScore -> "${playerA.name} wins the game."
            playerAScore < playerBScore  -> "${playerB.name} wins the game."
            else -> "Draw. No winner."
        }
    }

    override fun onGameFinished(playerAScore: Int, playerBScore: Int) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        p1Score.text = game.playerA.scoreString()
        p2Score.text = game.playerB.scoreString()
        gameResult.text = game.gameResultString()
    }

}