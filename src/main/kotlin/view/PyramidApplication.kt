package view

import entity.*
import service.*
import tools.aqua.bgw.core.BoardGameApplication
import java.util.*

/**
 * Constructs the main application for the Pyramid card game.
 * Initializes the game scenes and sets up the central service for game management.
 */
class PyramidApplication : BoardGameApplication("Pyramid"), Refreshable {

    /**
     * The central service from which all other services are created or accessed.
     * Holds the currently active game state and manages game logic.
     */
    private val rootService = RootService()

    /**
     * The main scene where the actual gameplay takes place.
     * Managed by the root service to reflect the current state of the game.
     */
    private val gameScene = GameScene(rootService)

    /**
     * The menu scene displayed after each game concludes, showing scores and options.
     * Allows players to start a new game or quit the application.
     */

    private val scoreBoardScene = GameFinishedMenuScene(rootService).apply {
        newGameButton.onMouseClicked = {
            this@PyramidApplication.showMenuScene(newGameMenuScene)
        }
        quitButton.onMouseClicked = {
            exit()
        }
    }

    /**
     * The menu scene shown at the application start and after selecting "new game" in the scoreBoardScene.
     * Provides options to start a new game or exit the application.
     */
    private val newGameMenuScene = NewGameMenuScene(rootService).apply {
        quitButton.onMouseClicked = {
            exit()
        }
    }

    /**
     * Initializes the application by registering scenes with the root service
     * and displaying the initial menu scene.
     */
    init {
        // all scenes and the application itself need too
        // react to changes done in the service layer
        rootService.addRefreshable(
            this,
            gameScene,
            scoreBoardScene,
            newGameMenuScene
        )
        rootService.playerService.addRefreshable(
            this,
            gameScene,
            scoreBoardScene,
            newGameMenuScene
        )

        this.showMenuScene(newGameMenuScene, 0)
        this.showGameScene(gameScene)
    }

    /**
     * Called when a new game starts.
     * Hides the menu scene and transitions to the game scene.
     *
     * @param name1 Name of the first player.
     * @param name2 Name of the second player.
     * @param pyramid Initial layout of the card pyramid.
     * @param drawPile Initial stack of cards for the draw pile.
     */
    override fun onGameStart(name1: String, name2: String, pyramid: MutableList<MutableList<Card?>>, drawPile: Stack<Card>) {
        this.hideMenuScene()
    }

    /**
     * Called when a game finishes.
     * Displays the scoreBoardScene to show final scores and present further options to the players.
     *
     * @param playerAScore Final score of player A.
     * @param playerBScore Final score of player B.
     */
    override fun onGameFinished(playerAScore: Int, playerBScore: Int) {
        this.showMenuScene(scoreBoardScene)

    }
}