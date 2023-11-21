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



class GameScene(private val root: RootService) : BoardGameScene (1920,1080), Refreshable {

    fun testDrawPile(currentGame: GameState) {
        val testCard = Card(CardSuit.SPADES, CardValue.FOUR)
        val cardView = initializeCardView(testCard, true)
        currentGame.table.drawPile.push(testCard)
        cardMap.add(testCard, cardView)
        addComponents(cardView)
    }

    private val drawPile = LabeledStackView(posX = 1600, posY = 750,"Draw Pile").apply { this.
        onMouseClicked ={ _: MouseEvent ->
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
    //private fun Table.pyramid(): String = "${this.drawCards.size}"
    private val pyramidView: MutableList<MutableList<CardView?>> = mutableListOf()

    private val selectedCards: MutableList<CardView> = mutableListOf()

    private var useReserve = false


    private fun initializePyramid(pyramidCards: List<Card>) {
        // Constants for card size and spacing
        val cardWidth = 100
        val cardHeight = 150
        val horizontalSpacing = 40
        val verticalSpacing = 30

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
                    cardView.posX = x.toDouble()
                    cardView.posY = y.toDouble()


                    addComponents(cardView)
                    pyramidView[row][col] = cardView
                    cardMap.add(card, cardView)

                    // Click event for each card
                    cardView.onMouseClicked = {
                        if (!selectedCards.contains(cardView) && selectedCards.size < 2) {
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

    private fun listListToList(listList: MutableList<MutableList<Card?>>): List<Card>{

        val toReturn= mutableListOf<Card>()
        listList.forEach { row ->
            row.forEach { it ->
                if (it != null) {
                    toReturn.add(it)
                }
            }

        }
        return toReturn
    }

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

        // Event handling for when a card is clicked

        return cardView
    }


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
            if (fromReserve != null && fromPyramid != null && isValidPair(fromReserve, fromPyramid)) {
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

            if (card1 != null && card2 != null && isValidPair(card1, card2)) {
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


    // Check if the sum of two card values is valid (equal to 15)
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


    private fun peekTopCardFromReserve(): Card? {
        return if (reservePile.isNotEmpty()) {
            // Peek at the top CardView from the reserve pile without removing it
            val topCardView = reservePile.peek()  // Assuming you have a peek method
            cardMap.backward(topCardView)
        } else {
            null
        }
    }

    //val as = emptyList<LabeledStackView>().toMutableList()
    //private val listOfTableCardsLabel = listOf(tableCard0, tableCard1, tableCard2).toMutableList()

    private val passButton = Button(
        width = 150, height = 150,
        posX = 1620, posY = 550,
        visual = ImageVisual("pass.png")
    ).apply {
        onMouseClicked = {
            root.playerService.actionSitOut()
        }
    }

    private val scoreLabel1 = Label(width = 300, height = 30, posX = 100, posY = 40,
        font = Font(size = 24,fontWeight = Font.FontWeight.BOLD)
    ).apply {
        text = "Player 1: 0"
    }

    private val scoreLabel2 = Label(width = 300, height = 30, posX = 1500, posY = 40,
        font = Font(size = 24,fontWeight = Font.FontWeight.BOLD)
    ).apply {
        text = "Player 2: 0"
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
        background = ColorVisual(244, 164, 96)
        addComponents(
            drawPile, reservePile,
            scoreLabel1,scoreLabel2,
            passButton,
            )

    }

    fun updatePlayerDisplay(player1Name: String, player2Name: String, currentPlayer: Player) {
        // Update player names and scores
        scoreLabel1.text = "$player1Name Score: ${root.currentGame?.playerA?.score}"
        scoreLabel2.text = "$player2Name Score: ${root.currentGame?.playerB?.score}"

        // Highlight the current player
        if (currentPlayer.name == player1Name) {
            scoreLabel1.visual = ColorVisual(Color.YELLOW)
            scoreLabel2.visual = ColorVisual(Color.WHITE)
        } else {
            scoreLabel1.visual = ColorVisual(Color.WHITE)
            scoreLabel2.visual = ColorVisual(Color.YELLOW)
        }
    }

    override fun onGameStart(name1: String, name2: String, pyramid: MutableList<MutableList<Card?>>, drawPile: Stack<Card>) {

        val game = root.currentGame
        checkNotNull(game) { "No started game found." }
        cardMap.clear()

        val cardImageLoader = CardImageLoader()

        updatePlayerDisplay(name1, name2, game.currentPlayer)
        testDrawPile(game)
        initializeStackView(drawPile, this.drawPile, cardImageLoader)

        initializePyramid(listListToList(pyramid))
    }

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

    override fun onScoreUpdate(score: Int, name: String) {
        // Update the score label for the corresponding player
        if (scoreLabel1.text.contains(name)) {
            scoreLabel1.text = "$name Score: $score"
        } else if (scoreLabel2.text.contains(name)) {
            scoreLabel2.text = "$name Score: $score"
        }
    }

    override fun onActionSitOut(nextPlayer: Player) {
        val game = root.currentGame ?: return // Ensure the game is not null

        val player1Name = game.playerA.name
        val player2Name = game.playerB.name

        // Update the player display with the new current player
        updatePlayerDisplay(player1Name, player2Name, nextPlayer)
    }

    override fun onActionDrawCard(nextPlayer: Player, drawnCard: Card) {
        val game = root.currentGame ?: return // Ensure the game is not null

        val cardView = cardMap.forward(drawnCard)

        cardView.showFront()

        drawPile.remove(cardView)
        reservePile.add(cardView)

        val player1Name = game.playerA.name
        val player2Name = game.playerB.name

        // Update the player display with the new current player
        updatePlayerDisplay(player1Name, player2Name, nextPlayer)
    }

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
        val game = root.currentGame ?: return // Ensure the game is not null
        val player1Name = game.playerA.name
        val player2Name = game.playerB.name

        // Update player scores and turn indicators
        updatePlayerDisplay(player1Name, player2Name, nextPlayer)

        // Optionally, you can also update the scores here if they are not automatically updated elsewhere
        onScoreUpdate(game.playerA.score, player1Name)
        onScoreUpdate(game.playerB.score, player2Name)
    }

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

    private fun revealCardAtPosition(position: Pair<Int, Int>) {
        val (row, col) = position
        if (row in pyramidView.indices && col in pyramidView[row].indices) {
            pyramidView[row][col]?.showFront()
        }
    }
}