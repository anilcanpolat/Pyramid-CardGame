package entity

import java.util.*

/**
 * Enum to distinguish between the 13 possible values in a french-suited card game:
 * 2-10, Jack, Queen, King, and Ace.
 *
 * The values are ordered according to their most common ordering:
 * 2 < 3 < ... < 10 < Jack < Queen < King < Ace
 *
 */
enum class CardValue {
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING,
    ACE,
    ;

    /**
     * Converts the card value to a string representation.
     * Numeric values are represented by their respective numbers, while face cards are
     * represented by their first letter ('J' for Jack, 'Q' for Queen, 'K' for King, 'A' for Ace).
     *
     * @return A string representation of the card value.
     */
    override fun toString() =
        when(this) {
            TWO -> "2"
            THREE -> "3"
            FOUR -> "4"
            FIVE -> "5"
            SIX -> "6"
            SEVEN -> "7"
            EIGHT -> "8"
            NINE -> "9"
            TEN -> "10"
            JACK -> "J"
            QUEEN -> "Q"
            KING -> "K"
            ACE -> "A"
        }

    /**
     * Converts the card value to its integer equivalent.
     * Numeric cards (2-10) are represented by their respective numbers.
     * Face cards are assigned values as follows: Jack (11), Queen (12), King (13), and Ace (1).
     *
     * @return An integer representing the card value.
     */
    fun toInteger(): Int = when (this) {
        TWO -> 2
        THREE -> 3
        FOUR -> 4
        FIVE -> 5
        SIX -> 6
        SEVEN -> 7
        EIGHT -> 8
        NINE -> 9
        TEN -> 10
        JACK -> 11
        QUEEN -> 12
        KING -> 13
        ACE -> 1
    }
    companion object {

        /**
         * Provides a set of card values for a reduced deck starting with the 7.
         * Useful for games that use a 32-card deck (7 through Ace).
         *
         * @return A set of CardValue starting from 7 to Ace.
         */
        fun shortDeck(): Set<CardValue> {
            return EnumSet.of(ACE, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING)
        }

    }




}