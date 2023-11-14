package view

import entity.*
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

class ScoreBoardScene: BoardGameScene(1920, 1080) {
    /**
     * Label component configuration. The label is set to cover the entire scene's width and height.
     * It is positioned at the top-left corner of the scene (posX = 0, posY = 0) and displays the greeting
     * message with a font size of 20.
     */
    private val helloLabel = Label(
        width = 500,
        height = 500,
        posX = 0,
        posY = 0,
        text = "Hello, SoPra!",
        font = Font(size = 20)
    )

    /**
     * Initialization block for the `HelloScene` class.
     * Sets the background color to a specific RGB value and adds the `helloLabel` component to the scene.
     */
    init {
        var background = ColorVisual(108, 168, 59)
        addComponents(helloLabel)
    }
}