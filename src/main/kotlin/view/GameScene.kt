package view

import entity.*

import service.*
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color
import service.CardImageLoader
import tools.aqua.bgw.event.MouseEvent
import java.util.*

/**
 * Constructs a GameScene with the given root service.
 * Initializes the game board with the necessary UI components and sets up event handlers.
 *
 * @param root The root service providing necessary game logic and state management.
 */
class GameScene(private val root: RootService) : BoardGameScene (1920,1080), Refreshable {

    //private fun Table.pyramid(): String = "${this.drawCards.size}"
    private val pyramidView: MutableList<MutableList<CardView?>> = mutableListOf()

    private val selectedCards: MutableList<CardView> = mutableListOf()

    private var useReserve = false

    private val drawPile = LabeledStackView(posX = 1600, posY = 750,"Draw Pile").apply {
        onMouseClicked ={
            root.playerService.actionDrawCard()
        }
    }

    private val reservePile = LabeledStackView(posX = 200, posY = 650, "Reserve Pile").apply {
        onMouseClicked = { _: MouseEvent ->
            if (this.isNotEmpty() && selectedCards.size < 2) {
                val topCardView = this.peek()
                if (!selectedCards.contains(topCardView)) {
                    selectedCards.add(topCardView)
                    useReserve = true

                    if (selectedCards.size == 2) {
                        handleCardSelection()
                    }
                }
            }
        }
    }

    private val scoreLabel1 = Label(width = 300, height = 30, posX = 100, posY = 40,
        font = Font(size = 24,fontWeight = Font.FontWeight.BOLD)
    ).apply {
        text = "${root.currentGame.playerA.name}: ${root.currentGame.playerA.score}"
    }

    private val scoreLabel2 = Label(width = 300, height = 30, posX = 1500, posY = 40,
        font = Font(size = 24,fontWeight = Font.FontWeight.BOLD)
    ).apply {
        text = "${root.currentGame.playerB.name}: ${root.currentGame.playerB.score}"
    }

    /**
     * Initializes a stack view with a given stack of cards.
     * Visually represents the stack in the UI.
     *
     * @param stack The stack of cards to be represented.
     * @param stackView The LabeledStackView to be initialized.
     * @param cardImageLoader The loader to retrieve card images.
     */
    private fun initializeStackView(stack: Stack<Card>, stackView: LabeledStackView, cardImageLoader: CardImageLoader) {
        stackView.clear()
        //stack.push(Card(CardSuit.SPADES, CardValue.FOUR))
        stack.forEach { card ->
            val cardView = CardView(
                height = 200,
                width = 130,
                front = ImageVisual(cardImageLoader.frontImageFor(card.suit, card.value)),
                back = ImageVisual(cardImageLoader.backImage)
            )
            stackView.add(cardView)
            cardMap.add(card, cardView)
        }
    }

    /**
     * Initializes the pyramid layout with the given set of cards.
     * Arranges cards in a pyramid structure and sets up their visual representation.
     *
     * @param pyramidCards List of cards to be arranged in the pyramid.
     */
    private fun initializePyramid(pyramidCards: List<Card>) {
        // Constants for card size and spacing
        val cardWidth = 100
        val cardHeight = 150
        val horizontalSpacing = 40
        val verticalSpacing = 15

        // Calculate the starting X and Y positions
        val startX = (this.width - cardWidth) / 2
        val startY = 50

        // Initialize each row of the pyramid layout
        for (row in 0 until 7) {
            val rowList = MutableList(row + 1) { null as CardView? }
            pyramidView.add(rowList)
        }

        var cardIndex = 0
        for (row in 0 until 7) {
            for (col in 0..row) {
                if (cardIndex < pyramidCards.size) {
                    val card = pyramidCards[cardIndex++]
                    val isEdgeCard = col == 0 || col == row
                    val cardView = initializeCardView(card, isEdgeCard)

                    // Calculate X and Y positions
                    val x = startX + col * (cardWidth + horizontalSpacing) - row * (cardWidth + horizontalSpacing) / 2
                    val y = startY + row * (cardHeight - verticalSpacing)

                    // Set positions and add to scene
                    cardView.posX = x
                    cardView.posY = y.toDouble()

                    addComponents(cardView)
                    pyramidView[row][col] = cardView
                    cardMap.add(card, cardView)

                    // Click event for each card
                    cardView.onMouseClicked = {
                        if (cardView.currentSide == CardView.CardSide.FRONT &&!selectedCards.contains(cardView)
                            && selectedCards.size < 2) {
                                selectedCards.add(cardView)
                                if (selectedCards.size == 2) {
                                    handleCardSelection()
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Converts a list of lists of cards into a single flattened list.
     *
     * @param listList The list of lists to be flattened.
     * @return A single list containing all the cards.
     */
    private fun listListToList(listList: MutableList<MutableList<Card?>>): List<Card>{

        val toReturn= mutableListOf<Card>()
        listList.forEach { row ->
            row.forEach {
                if (it != null) {
                    toReturn.add(it)
                }
            }
        }
        return toReturn
    }

    /**
     * Initializes a CardView for a given card.
     * Sets up the visual representation of the card, including its front and back images.
     *
     * @param card The card to be visualized.
     * @param isEdgeCard Indicates if the card is an edge card, affecting its initial visibility.
     * @return A CardView representing the given card.
     */
    private fun initializeCardView(card: Card, isEdgeCard: Boolean): CardView {
        val cardImageLoader = CardImageLoader()

        val cardView = CardView(
            width = 100,
            height = 150,
            front = ImageVisual(cardImageLoader.frontImageFor(card.suit, card.value)),
            back = ImageVisual(cardImageLoader.backImage)
        )

        // Show the front for edge cards, back for non-edge cards
        if (isEdgeCard) {
            cardView.showFront()
        } else {
            cardView.showBack()
        }

        return cardView
    }

    /**
     * Handles the selection of cards by the player.
     * Processes the selected cards, checks if they form a valid pair, and performs the necessary game actions.
     */
    private fun handleCardSelection() {
        if (useReserve && selectedCards.size == 2) {
            // Determine which card is from the reserve and which is from the pyramid
            val fromReserve: Card?
            val fromPyramid: Card?

            if (peekTopCardFromReserve() == cardMap.backward(selectedCards[0])) {
                fromReserve = cardMap.backward(selectedCards[0])
                fromPyramid = cardMap.backward(selectedCards[1])
            } else {
                fromReserve = cardMap.backward(selectedCards[1])
                fromPyramid = cardMap.backward(selectedCards[0])
            }

            // Check if the pair is valid and process accordingly
            if (isValidPair(fromReserve, fromPyramid)) {
                root.playerService.actionRemovePair(fromReserve, fromPyramid, useReserve)

                reservePile.pop() // Pop from reserve if it's a part of the pair

                selectedCards.forEach { cardView ->
                    this.removeComponents(cardView)
                }

                selectedCards.clear()
                useReserve = false
            } else {
                // Invalid pair selected, reset the selection
                selectedCards.clear()
                useReserve = false
            }
        } else if (!useReserve && selectedCards.size == 2) {
            // Handle the case where both cards are from the pyramid
            val card1 = cardMap.backward(selectedCards[0])
            val card2 = cardMap.backward(selectedCards[1])

            if (isValidPair(card1, card2)) {
                root.playerService.actionRemovePair(card1, card2, false)

                // Remove card views from UI
                selectedCards.forEach { cardView ->
                    this.removeComponents(cardView)
                }

                selectedCards.clear()
            } else {
                // Invalid pair selected, reset the selection
                selectedCards.clear()
            }
        }
    }

    /**
     * Checks if two cards form a valid pair according to the game's rules.
     *
     * @param cardA The first card in the pair.
     * @param cardB The second card in the pair.
     * @return True if the pair is valid, false otherwise.
     */
    private fun isValidPair(cardA: Card, cardB: Card): Boolean {
        if (cardA.value == CardValue.ACE && cardB.value == CardValue.ACE) {
            // Two Aces cannot form a pair
            return false
        }
        if (cardA.value == CardValue.ACE || cardB.value == CardValue.ACE) {
            // If either card is an Ace, it's a valid pair
            return true
        }

        val sum = cardA.value.toInteger() + cardB.value.toInteger()
        return sum == 15
    }

    /**
     * Peeks at the top card from the reserve pile.
     *
     * @return The top card from the reserve pile, or null if the pile is empty.
     */
    private fun peekTopCardFromReserve(): Card? {
        return if (reservePile.isNotEmpty()) {
            // Peek at the top CardView from the reserve pile without removing it
            val topCardView = reservePile.peek()  // Assuming you have a peek method
            cardMap.backward(topCardView)
        } else {
            null
        }
    }

    private val passButton = Button(
        width = 150, height = 150,
        posX = 1620, posY = 550,
        visual = ImageVisual("pass.png")
    ).apply {
        onMouseClicked = {
            root.playerService.actionSitOut()
        }
    }

    /**
     * structure to hold pairs of (card, cardView) that can be used
     *
     * 1. to find the corresponding view for a card passed on by a refresh method (forward lookup)
     *
     * 2. to find the corresponding card to pass to a service method on the occurrence of
     * ui events on views (backward lookup).
     */
    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    init {

        background = ImageVisual("desert.png")
        addComponents(
            drawPile, reservePile,
            scoreLabel1,scoreLabel2,
            passButton,
            )
    }

    /**
    * Updates the display of player names and scores.
    * Highlights the current player and refreshes score labels according to the game state.
    *
    * @param player1Name Name of the first player.
    * @param player2Name Name of the second player.
    * @param currentPlayer The player whose turn is currently active.
    */
    private fun updatePlayerDisplay(player1Name: String, player2Name: String, currentPlayer: Player) {
        // Update player names and scores
        scoreLabel1.text = "$player1Name Score: ${root.currentGame.playerA.score}"
        scoreLabel2.text = "$player2Name Score: ${root.currentGame.playerB.score}"

        // Highlight the current player
        if (currentPlayer.name == player1Name) {
            scoreLabel1.visual = ColorVisual(Color.YELLOW)
            scoreLabel2.visual = ColorVisual(Color.WHITE)
        } else {
            scoreLabel1.visual = ColorVisual(Color.WHITE)
            scoreLabel2.visual = ColorVisual(Color.YELLOW)
        }
    }

    /**
     * Sets up the game board at the start of the game.
     * Initializes the card pyramid, draw pile, and other elements based on the initial game state.
     *
     * @param name1 Name of the first player.
     * @param name2 Name of the second player.
     * @param pyramid Initial card arrangement for the pyramid.
     * @param drawPile Initial set of cards for the draw pile.
     */
    override fun onGameStart(name1: String, name2: String, pyramid: MutableList<MutableList<Card?>>, drawPile: Stack<Card>) {

        val game = root.currentGame
        cardMap.clear()

        val cardImageLoader = CardImageLoader()

        updatePlayerDisplay(name1, name2, game.currentPlayer)
        initializeStackView(drawPile, this.drawPile, cardImageLoader)

        initializePyramid(listListToList(pyramid))
    }

    /**
     * Updates the score display for a player.
     * Called when there is a change in the score during the game.
     *
     * @param score The new score to be displayed.
     * @param name The name of the player whose score is updated.
     */
    override fun onScoreUpdate(score: Int, name: String) {
        // Update the score label for the corresponding player
        if (scoreLabel1.text.contains(name)) {
            scoreLabel1.text = "$name Score: $score"
        } else if (scoreLabel2.text.contains(name)) {
            scoreLabel2.text = "$name Score: $score"
        }
    }

    /**
     * Updates the game state when a player chooses to sit out a turn.
     * Reflects changes in the UI to indicate the next player's turn.
     *
     * @param nextPlayer The player who will take the next turn.
     */
    override fun onActionSitOut(nextPlayer: Player) {
        val game = root.currentGame // Ensure the game is not null

        val player1Name = game.playerA.name
        val player2Name = game.playerB.name

        // Update the player display with the new current player
        updatePlayerDisplay(player1Name, player2Name, nextPlayer)
    }

    /**
     * Handles the action of drawing a card from the draw pile.
     * Updates the game state and UI to reflect the drawn card and the next player's turn.
     *
     * @param nextPlayer The player who will take the next turn.
     * @param drawnCard The card that has been drawn.
     */
    override fun onActionDrawCard(nextPlayer: Player, drawnCard: Card) {
        val game = root.currentGame// Ensure the game is not null

        val cardView = cardMap.forward(drawnCard)

        cardView.showFront()

        drawPile.remove(cardView)
        reservePile.add(cardView)

        val player1Name = game.playerA.name
        val player2Name = game.playerB.name

        // Update the player display with the new current player
        updatePlayerDisplay(player1Name, player2Name, nextPlayer)
    }

    /**
     * Handles the removal of a pair of cards from the game.
     * Updates the game state and UI to reflect the removal of the cards and any newly visible cards.
     *
     * @param nextPlayer The player who will take the next turn.
     * @param cardA Position of the first card in the pair.
     * @param cardB Position of the second card in the pair.
     * @param reserveTop The top card of the reserve pile, if applicable.
     * @param nowVisible A list of positions of cards that are now visible.
     */
    override fun onActionRemovePair(
        nextPlayer: Player,
        cardA: Pair<Int, Int>?,
        cardB: Pair<Int, Int>?,
        reserveTop: Card?,
        nowVisible: MutableList<Pair<Int, Int>>
    ) {
        // Remove or hide the CardViews for cardA and cardB
        if (cardA != null) {
            removeCardFromPyramidView(cardA)
        }
        if (cardB != null) {
            removeCardFromPyramidView(cardB)
        }

        // Update the reserve pile top card display (if needed)
        // updateReservePileDisplay(reserveTop)

        // Reveal newly visible cards in the pyramid
        nowVisible.forEach { position -> revealCardAtPosition(position) }

        // Retrieve game state and player names
        val game = root.currentGame // Ensure the game is not null
        val player1Name = game.playerA.name
        val player2Name = game.playerB.name

        // Optionally, you can also update the scores here if they are not automatically updated elsewhere
        onScoreUpdate(game.playerA.score, player1Name)
        onScoreUpdate(game.playerB.score, player2Name)

        // Update player scores and turn indicators
        //updatePlayerDisplay(player1Name, player2Name, nextPlayer)

    }

    /**
     * Removes a card from the pyramid view.
     * Called when a card is successfully removed from the game.
     *
     * @param position The position of the card in the pyramid to be removed.
     */
    private fun removeCardFromPyramidView(position: Pair<Int, Int>) {
        val (row, col) = position
        val cardView = pyramidView[row][col]

        // Remove the CardView from the layout
        if (cardView != null) {
            this.removeComponents(cardView)
        }

        // Set the corresponding position in pyramidLayout to null
        pyramidView[row][col] = null
    }

    /**
     * Reveals a card at a specific position in the pyramid.
     * Used when a card becomes visible due to the removal of other cards.
     *
     * @param position The position of the card in the pyramid to be revealed.
     */
    private fun revealCardAtPosition(position: Pair<Int, Int>) {
        val (row, col) = position
        if (row in pyramidView.indices && col in pyramidView[row].indices) {
            pyramidView[row][col]?.showFront()
        }
    }
}