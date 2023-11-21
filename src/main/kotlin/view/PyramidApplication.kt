package view

import entity.*
import service.*
import tools.aqua.bgw.core.BoardGameApplication
import java.util.*

class PyramidApplication : BoardGameApplication("Pyramid"), Refreshable {

    // Central service from which all others are created/accessed
    // also holds the currently active game
    private val rootService = RootService()

    // Scenes

    // This is where the actual game takes place
    private val gameScene = GameScene(rootService)


    // This menu scene is shown after each finished game (i.e. no more cards to draw)
    private val scoreBoardScene = GameFinishedMenuScene(rootService).apply {
        newGameButton.onMouseClicked = {
            this@PyramidApplication.showMenuScene(newGameMenuScene)
        }
        quitButton.onMouseClicked = {
            exit()
        }
    }

    // This menu scene is shown after application start and if the "new game" button
    // is clicked in the gameFinishedMenuScene
    private val newGameMenuScene = NewGameMenuScene(rootService).apply {
        quitButton.onMouseClicked = {
            exit()
        }
    }

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
    override fun onGameStart(name1: String, name2: String, pyramid: MutableList<MutableList<Card?>>, drawPile: Stack<Card>) {
        this.hideMenuScene()
    }

    override fun onGameFinished(playerAScore: Int, playerBScore: Int) {
        this.showMenuScene(scoreBoardScene)

    }
}