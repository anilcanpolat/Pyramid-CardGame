package service

import entity.*
import java.lang.IllegalStateException

class PlayerService(private val gameState: GameState) {

    //Returns the position of card as Integer
    private fun getPosOfCard(targetCard: Card): Pair<Int, Int>? {
        var currentPosition = 0 // This will be the position in a flattened version of the pyramid

        // Iterate through each row of the pyramid
        for (row in 0 until 7) {
            // Iterate through each card in the row
            for (column in 0 until row+1) {
                // Check if the current card is the one we're looking for
                val card = gameState.table.pyramid[row][column]
                if (card == targetCard) {
                    return Pair(row,column)
                }
                // Increment the currentPosition for the next card
                currentPosition++
            }
        }
        // If the card is not found, return null
        return null
    }

    //Validates, if the cards has in total of the value 15
    private fun isValidPair(cardA: Card, cardB: Card): Int {
        if (cardA.value == 1 && cardB.value == 1) {
            // Two Aces cannot form a pair
            return -1
        }
        if (cardA.value == 1 || cardB.value == 1) {
            // If either card is an Ace, it's a valid pair
            return 1
        }
        if(cardA.value + cardB.value == 15) {
            return 2
        }
        return -1
    }

    //Removes from the pyramid when a given Card is existent in a pyramid
    private fun removeFromPyramid(card: entity.Card) {
        /*//Get the Position of the CardB as Array<Pair<Int, Int>?>
        val cardPosition = getPosOfCard(Card)?.let { findPosition(it) }
        //Checks if the position is not null, as it is nullable*/
        val cardPosition = getPosOfCard(card)
        if (cardPosition != null) {
            val (row, column) = cardPosition
            //Sets the Card to null
            gameState.table.pyramid[row][column] = null
        }
    }

    //Looks if the given Cards is in reserveStack, removes the 2 Cards accordingly
    fun actionRemovePair(
        CardA: entity.Card,
        CardB : entity.Card,
        useReserve: Boolean) : Unit {
        //Validates that, they can be unveiled
        val point = isValidPair(CardA, CardB)
        if(point > 0) {
            //There is at one Card in the reserve that is going to be nulled
            if(useReserve) {
                //CardA is on the reserveStack
                if(getPosOfCard(CardA) == null && getPosOfCard(CardB) != null) {
                    removeFromPyramid(CardB)
                    gameState.table.reserveStack.pop()  //Sets the Card on the ReserveStack null
                }
                //CardB is on the reserveStack
                else if(getPosOfCard(CardA) != null && getPosOfCard(CardB) == null) {
                    removeFromPyramid(CardA)
                    gameState.table.reserveStack.pop()  //Sets the Card on the ReserveStack null
                }
                else {
                    throw IllegalStateException("There isn't any Card to remove in the pyramid")
                }
            }
            else{ //Cards are on the Pyramid
                removeFromPyramid(CardA)
                removeFromPyramid(CardB)
            }
            if(point == 1) gameState.currentPlayer.score++
            if(point == 2) gameState.currentPlayer.score+= 2
            gameState.sitOutCount = 0;
            gameState.switchCurrentPlayer()
        }
    }

    //Draws a Card from the drawStack and makes it visible
    fun actionDrawCard() : Unit {
        //Check if there are cards remaining in the draw pile
        if (gameState.table.drawPile.isNullOrEmpty()) {
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
    }

    //Increments sitOutCount and passes one round
    fun actionSitOut() : Unit {
        gameState.switchCurrentPlayer()
        gameState.sitOutCount++
    }

    fun currentPlayerName() : String {
        return gameState.currentPlayer.name
    }
}