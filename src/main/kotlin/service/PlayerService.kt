package service

import entity.*
import java.lang.IllegalStateException

/**
 * The PlayerService class is responsible for managing the game state related to player actions
 * within a card game. It provides functionalities for making neighbour cards visible,
 * getting the position of a specific card, validating card pairs, removing cards from the pyramid,
 * handling card pair removal action, drawing a card from the draw stack, and allowing a player to sit out.
 */
class PlayerService(val rootService: RootService): AbstractRefreshingService() {

    /**
     * Makes neighbouring cards visible when a card is removed from the pyramid.
     *
     * @param row the row index of the card.
     * @param column the column index of the card.
     */
    private fun makeNeighboursVisible(row: Int, column: Int): MutableList<Card?> {

        val gameState = rootService.currentGame

        val nowVisibleCards: MutableList<Card?> = mutableListOf()

        // Left neighbor
        if (column > 0 && gameState.table.pyramid[row][column - 1] != null) {
            gameState.table.pyramid[row][column - 1]?.visible = true
            nowVisibleCards.add(gameState.table.pyramid[row][column - 1])
        }

        // Right neighbor
        if (column < gameState.table.pyramid[row].size - 1 && gameState.table.pyramid[row][column + 1] != null) {
            gameState.table.pyramid[row][column + 1]?.visible = true
            nowVisibleCards.add(gameState.table.pyramid[row][column + 1])
        }
        return nowVisibleCards
    }


    /**
     * Retrieves the position of the specified card in the pyramid.
     *
     * @param targetCard the card whose position is to be found.
     * @return a pair of integers representing the row and column of the card if found, otherwise null.
     */
    private fun getPosOfCard(targetCard: Card): Pair<Int, Int>? {

        //if(rootService.currentGame.table.pyramid.forEach{it -> it.contains(targetCard)})  {

            val gameState = rootService.currentGame

            var currentPosition = 0 // This will be the position in a flattened version of the pyramid

            // Iterate through each row of the pyramid
            for (row in 0 until 7) {
                // Iterate through each card in the row
                for (column in 0 until row + 1) {
                    // Check if the current card is the one we're looking for
                    val card = gameState.table.pyramid[row][column]
                    if (card == targetCard) {
                        return Pair(row, column)
                    }
                    // Increment the currentPosition for the next card
                    currentPosition++
                }
            }
            // If the card is not found, return null
        //}
        return null
    }

    /**
     * Validates if the pair of cards have a total value of 15, which is considered valid in the game.
     *
     * @param cardA the first card.
     * @param cardB the second card.
     * @return an integer code representing the validation result.
     */
    private fun isValidPair(cardA: Card, cardB: Card): Int {
        if (cardA.value == CardValue.ACE && cardB.value ==  CardValue.ACE) {
            // Two Aces cannot form a pair
            return -1
        }
        if (cardA.value == CardValue.ACE || cardB.value == CardValue.ACE) {
            // If either card is an Ace, it's a valid pair
            return 1
        }
        if (cardA.value.toInteger() + cardB.value.toInteger() == 15) {
            return 2
        }
        return -1
    }

    /**
     * Removes a card from the pyramid structure if it exists.
     *
     * @param card the card to be removed from the pyramid.
     */
    private fun removeFromPyramid(card: Card): MutableList<Card?> {

        val gameState = rootService.currentGame

        //Checks if the position is not null, as it is nullable*/
        val cardPosition = getPosOfCard(card)
        if (cardPosition != null) {
            val (row, column) = cardPosition
            //Sets the Card to null
            gameState.table.pyramid[row][column] = null
            return makeNeighboursVisible(row, column)
        }
        return mutableListOf()
    }

    /**
     * Performs the action of removing a pair of cards from the pyramid or the reserve stack.
     *
     * @param cardA the first card.
     * @param cardB the second card.
     * @param useReserve a boolean indicating if the reserve stack should be used.
     */
    fun actionRemovePair(
        cardA: Card,
        cardB: Card,
        useReserve: Boolean
    ) {
        val gameState = rootService.currentGame

        var reserveTop: Card? = null
        if(!gameState.table.reserveStack.empty()) reserveTop = gameState.table.reserveStack.last()

        //Validates that, they can be unveiled
        val indicator = isValidPair(cardA, cardB)
        val posA = getPosOfCard(cardA)
        val posB = getPosOfCard(cardB)
        val nowVisibleCards: MutableList<Card?>
        if (indicator > 0) {
            //There is at one Card in the reserve that is going to be nulled
            if (useReserve) {
                //CardA is on the reserveStack
                if (getPosOfCard(cardA) == null && getPosOfCard(cardB) != null) {
                    nowVisibleCards = removeFromPyramid(cardB)
                    gameState.table.reserveStack.pop()  //Sets the Card on the ReserveStack null
                }
                //CardB is on the reserveStack
                else if (getPosOfCard(cardA) != null && getPosOfCard(cardB) == null) {
                    nowVisibleCards = removeFromPyramid(cardA)
                    gameState.table.reserveStack.pop()  //Sets the Card on the ReserveStack null
                } else {
                    throw IllegalStateException("There isn't any Card to remove in the pyramid")
                }
            } else { //Cards are on the Pyramid
                nowVisibleCards = removeFromPyramid(cardA)
                nowVisibleCards.addAll(removeFromPyramid(cardB))
            }
            if (indicator == 1) gameState.currentPlayer.score++
            if (indicator == 2) gameState.currentPlayer.score += 2

            onAllRefreshables {
                onScoreUpdate(
                    gameState.currentPlayer.score,
                    gameState.currentPlayer.name
                )
            }

            gameState.sitOutCount = 0
            gameState.switchCurrentPlayer()

            val nowVisiblePositions: MutableList<Pair<Int, Int>?> = mutableListOf()
            for (card in nowVisibleCards) {
                nowVisiblePositions.add(card?.let { getPosOfCard(it) })
            }

            // Filter out the null values before passing the list
            val nonNullableNowVisiblePositions = nowVisiblePositions.filterNotNull().toMutableList()

            onAllRefreshables {
                if (posA != null && posB != null) {
                    onActionRemovePair(
                        gameState.currentPlayer,
                        posA, posB, reserveTop,
                        nonNullableNowVisiblePositions // pass the non-nullable list here
                    )
                }
            }
        }
    }
    /**
     * Draws a card from the draw stack, makes it visible, and adds it to the reserve stack.
     */
    fun actionDrawCard() {
        val gameState = rootService.currentGame

        //Check if there are cards remaining in the draw pile
        if (gameState.table.drawPile.isEmpty()) {
            //No cards left in the draw pile
            throw IllegalStateException("There is no card to be drawn from the pile")
        }
        //Card is drawn, made visible and added to the reserveStack
        val drawnCard = gameState.table.drawPile.pop()
        drawnCard.visible = true
        gameState.table.reserveStack.push(drawnCard)
        //sitOutCount is refreshed and player is switched
        gameState.sitOutCount = 0
        gameState.switchCurrentPlayer()
        onAllRefreshables { onActionDrawCard(gameState.currentPlayer, drawnCard) }
    }

    /**
     * Increments the sit out count and passes the turn to the next player.
     */
    fun actionSitOut() {
        val gameState = rootService.currentGame

        gameState.switchCurrentPlayer()
        gameState.sitOutCount++

        onAllRefreshables { onActionSitOut(gameState.currentPlayer) }
    }
}
