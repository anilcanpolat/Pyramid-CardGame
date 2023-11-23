package view

import service.*
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/**
 * Constructs a new game menu scene.
 * Initializes UI components for entering player names and starting or quitting a new game.
 *
 * @param rootService The root service to manage game logic and state transitions.
 */
class NewGameMenuScene(private val rootService: RootService) : MenuScene(480, 720 ), Refreshable {

    /**
     * A label displaying the headline "Start New Game".
     * Positioned at the top of the menu for clear visibility.
     */
    private val headlineLabel = Label(
        width = 300, height = 50, posX = 80, posY = 50,
        text = "Start New Game",
        font = Font(size = 22)
    )

    /**
     * A label for Player 1's name input field.
     * Indicates where Player 1 should enter their name.
     */
    private val p1Label = Label(
        width = 100, height = 35,
        posX = 50, posY = 125,
        text = "Player 1:"
    )

    /**
     * A text field for Player 1 to enter their name.
     * Randomly selects a default name from a predefined list.
     * Disables the start button if either player's name field is blank.
     */
    private val p1Input: TextField = TextField(
        width = 200, height = 35,
        posX = 150, posY = 125,
        text = listOf("Homer", "Marge", "Bart", "Lisa", "Maggie").random()
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = this.text.isBlank() || p2Input.text.isBlank()
        }
    }

    /**
     * A label for Player 2's name input field.
     * Indicates where Player 2 should enter their name.
     */
    private val p2Label = Label(
        width = 100, height = 35,
        posX = 50, posY = 170,
        text = "Player 2:"
    )

    /**
     * A text field for Player 2 to enter their name.
     * Randomly selects a default name from a predefined list.
     * Disables the start button if either player's name field is blank.
     */
    private val p2Input: TextField = TextField(
        width = 200, height = 35,
        posX = 150, posY = 170,
        text = listOf("Fry", "Bender", "Leela", "Amy", "Zoidberg").random()
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = p1Input.text.isBlank() || this.text.isBlank()
        }
    }

    /**
     * A button to quit the current operation or exit the game.
     * Styled with a distinct visual to indicate its purpose.
     */
    val quitButton = Button(
        width = 140, height = 35,
        posX = 50, posY = 240,
        text = "Quit"
    ).apply {
        visual = ColorVisual(221, 136, 136)
    }

    /**
     * A button to start the game with the entered player names.
     * Initiates the game setup process in the root service when clicked.
     * Disabled by default until both player names are entered.
     */
    private val startButton = Button(
        width = 140, height = 35,
        posX = 210, posY = 240,
        text = "Start"
    ).apply {
        visual = ColorVisual(136, 221, 136)
        onMouseClicked = {
            rootService.startGame(
                p1Input.text.trim(),
                p2Input.text.trim()
            )
        }
    }

    /**
     * Initializes the scene with the defined UI components.
     * Sets up the layout, positioning, and initial properties of each component.
     */
    init {
        //background = ImageVisual("background.png")
        opacity = .5
        addComponents(
            headlineLabel,
            p1Label, p1Input,
            p2Label, p2Input,
            startButton, quitButton
        )
    }
}