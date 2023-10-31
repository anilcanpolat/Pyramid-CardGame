package view

import tools.aqua.bgw.core.BoardGameApplication

/**
 * `SopraApplication` class represents the main application for the SoPra game.
 * It extends the `BoardGameApplication` class from the `tools.aqua.bgw.core` package.
 *
 * Upon instantiation, it initializes and displays the `HelloScene` which showcases a greeting message.
 *
 * @property helloScene An instance of the `HelloScene` class.
 *
 * @constructor Creates an instance of the `SopraApplication` class with the title "SoPra Game".
 */
class SopraApplication : BoardGameApplication("SoPra Game") {

    /**
     * Creates and holds an instance of the `HelloScene`.
     */
    private val helloScene = HelloScene()

    /**
     * Initialization block.
     * Sets and displays the `helloScene` as the main scene of the application.
     */
    init {
        this.showGameScene(helloScene)
    }

}
