package service

import entity.*
import java.lang.IllegalStateException

class PlayerService(private val gameState: GameState) {

    //Returns the position of card as Integer
    private fun getPosOfCard(targetCard: Card): Int? {
        var currentPosition = 0 // This will be the position in a flattened version of the pyramid

        // Iterate through each row of the pyramid
        for (row in gameState.table.pyramid) {
            // Iterate through each card in the row
            for (card in row) {
                // Check if the current card is the one we're looking for
                if (card == targetCard) {
                    return currentPosition
                }
                // Increment the currentPosition for the next card
                currentPosition++
            }
        }
        // If the card is not found, return null
        return null
    }

    //Returns the array+1 position of an element in 2-dimensional List in the form or Pair<Int,Int>
    private fun findPosition(number: Int): Pair<Int, Int>? {
        if (number < 1 || number > 28) {
            return null // The number is out of bounds
        }

        var remaining = number
        var row = 1

        while (remaining > row) {
            remaining -= row
            row += 1
        }

        // At this point, 'row' is the row of the pyramid, and 'remaining' is the column in that row
        return Pair(row, remaining)
    }

    //Validates, if the cards has in total of the value 15
    private fun isValidPair(cardA: Card, cardB: Card): Boolean {
        if (cardA.value == 1 && cardB.value == 1) {
            // Two Aces cannot form a pair
            return false
        }
        if (cardA.value == 1 || cardB.value == 1) {
            // If either card is an Ace, it's a valid pair
            return true
        }
        val sum = cardA.value + cardB.value
        return sum == 15
    }

    //Removes from the pyramid when a given Card is existent in a pyramid
    private fun removeFromPyramid(Card: entity.Card) {
        //Get the Position of the CardB as Array<Pair<Int, Int>?>
        val cardPosition = getPosOfCard(Card)?.let { findPosition(it) }
        //Checks if the position is not null, as it is nullable
        if (cardPosition != null) {
            val (row, column) = cardPosition
            // Adjusting for zero-based index since we are accessing lists
            val rowIndex = row - 1
            val columnIndex = column - 1
            //Sets the Card to null
            gameState.table.pyramid[rowIndex][columnIndex] = null
        }
    }

    //Looks if the given Cards is in reserveStack, removes the 2 Cards accordingly
    fun actionRemovePair(
        CardA: entity.Card,
        CardB : entity.Card,
        useReserve: Boolean) : Unit {
        //Validates that, they can be unveiled
        if(isValidPair(CardA, CardB)) {
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
            gameState.sitOutCount = 0;
            gameState.currentPlayer.score++
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

    //Gives out the Card in a specific position
    private fun getCardAtPos(pos: Int): Card? {

        if (pos < 0) {
            throw IllegalArgumentException("Position cannot be negative")
        }
        var currentPos = 0
        for (row in gameState.table.pyramid) {
            // Check if the position is within the current row
            if (pos < currentPos + row.size) {
                // The index within the row is the position minus the sum of all previous rows' sizes
                return row[pos - currentPos]
            }
            // Update the current position to account for the size of the current row
            currentPos += row.size
        }

        // If the position is out of bounds of the pyramid, return null or throw an exception
        return null
    }

    fun roundService(root: RootService) {
        // Check if the game has already finished
        if (gameFinished()) {
            return // Exit the method if the game is over
        }


        //Not yet finished

    }

    fun currentPlayerName() : String {
        return gameState.currentPlayer.name
    }

    //If 2 subsequent pass occurs or pyramid is completely empty game ends
    fun gameFinished() : Boolean {

        if (gameState.playerA.score > gameState.playerB.score) println("${gameState.playerA.name} has won")
        else if(gameState.playerA.score < gameState.playerB.score) println("${gameState.playerA.name} has won")
        else println("It's a draw ¯\\_(ツ)_/¯")

        return gameState.sitOutCount == 2 ||
                gameState.table.pyramid.all { row ->
                    row.all { it == null }
                }
    }
}