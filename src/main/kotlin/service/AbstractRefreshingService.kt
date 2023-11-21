package service

import view.GameFinishedMenuScene
import view.GameScene
import view.NewGameMenuScene
import view.Refreshable

abstract class AbstractRefreshingService {

    private val refreshables = mutableListOf<Refreshable>()
    /**
     * adds a [Refreshable] to the list that gets called
     * whenever [onAllRefreshables] is used.
     */
    open fun addRefreshable(
        newRefreshable: Refreshable,
        gameScene: GameScene,
        gameFinishedMenuScene: GameFinishedMenuScene,
        newGameMenuScene: NewGameMenuScene
    ) {
        refreshables += newRefreshable
        refreshables += gameScene
        refreshables += newGameMenuScene
    }

    /**
     * Executes the passed method (usually a lambda) on all
     * [Refreshable]s registered with the service class that
     * extends this [AbstractRefreshingService]
     *
     * Example usage (from any method within the service):
     * ```
     * onAllRefreshables {
     *   refreshPlayerStack(p1, p1.playStack)
     *   refreshPlayerStack(p2, p2.playStack)
     *   refreshPlayerStack(p1, p1.collectedCardsStack)
     *   refreshPlayerStack(p2, p2.collectedCardsStack)
     * }
     * ```
     *
     */
    fun onAllRefreshables(method: Refreshable.() -> Unit) =
        refreshables.forEach {
            it.method()
        }
}