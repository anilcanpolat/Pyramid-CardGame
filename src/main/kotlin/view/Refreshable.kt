package view
import service.*
import entity.*
import java.util.Stack

interface Refreshable {

    fun onScoreUpdate(score: Int,
                      name: String): Unit

    fun onGameFinished(playerAScore_Name: Int,
                       playerBScore_Name: Int): Unit

    fun onGameStart(
        name1: String,
        name2: String,
        pyramid: MutableList<MutableList<Card?>>,
        drawPile: Stack<Card>
    ): Unit

    fun onActionRemovePair(
        nextPlayer: Player,
        cardA: Pair<Int,Int>,
        cardB: Pair<Int,Int>,
        reserveTop: Card?,
        nowVisible: List <Pair<Int, Int>>
    ): Unit

    fun onActionDrawCard(
        nextPlayer: Player,
        drawnCard: Card
    ): Unit

    fun onActionSitOut(
        nextPlayer: Player
    ): Unit
}