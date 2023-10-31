package view

import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/**
 * `HelloScene` class represents a scene specifically designed to display a greeting message.
 *
 * This class is an extension of the `BoardGameScene` provided by the `tools.aqua.bgw.core` package.
 * It initializes a scene with a set width and height of 500 units each and configures a label
 * to display the greeting message "Hello, SoPra!".
 *
 * @property helloLabel A label component that contains the greeting message and is centered within the scene.
 */
class HelloScene : BoardGameScene(500, 500) {

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
        background = ColorVisual(108, 168, 59)
        addComponents(helloLabel)
    }
}
