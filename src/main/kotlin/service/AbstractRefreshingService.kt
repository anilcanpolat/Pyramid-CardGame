package service

import view.ScoreBoardScene
import view.GameScene
import view.NewGameMenuScene
import view.Refreshable

/**
 * Abstract class that provides a mechanism to refresh UI components in a game application.
 * This class maintains a list of 'Refreshable' objects, allowing for consistent and synchronized
 * updates across various UI components when the game state changes.
 *
 * It offers a centralized way to manage refresh operations, ensuring that all parts of the UI
 * are kept up-to-date with the current state of the game. This is particularly useful in complex
 * applications with multiple views or scenes, such as in a card or board game.
 */
abstract class AbstractRefreshingService {

    // Holds a list of all refreshable UI components.
    val refreshables = mutableListOf<Refreshable>()
    /**
     * Adds a [Refreshable] object to the service's list of refreshables.
     * Once added, these objects can be collectively updated through the 'onAllRefreshables' method.
     * This method is designed to be flexible, allowing various types of UI components to be refreshed.
     *
     * @param newRefreshable The new [Refreshable] object to be added for updates.
     * @param gameScene The main game scene, representing the primary view of the game.
     * @param gameFinishedMenuScene The scene to be displayed when the game finishes.
     * @param newGameMenuScene The scene displayed for starting a new game.
     */
    open fun addRefreshable(
        newRefreshable: Refreshable,
        gameScene: GameScene,
        gameFinishedMenuScene: ScoreBoardScene,
        newGameMenuScene: NewGameMenuScene
    ) {
        refreshables += newRefreshable
        refreshables += gameScene
        refreshables += newGameMenuScene
    }

    /**
     * Executes a given method (typically a lambda expression) on all registered [Refreshable] objects.
     * This method is used for bulk operations where all UI components need to reflect a change in state.
     *
     * Example usage:
     * ```
     * onAllRefreshables {
     *   refreshPlayerStack(p1, p1.playStack)
     *   refreshPlayerStack(p2, p2.playStack)
     * }
     * ```
     * @param method The method (lambda function) to be executed on all registered [Refreshable] objects.
     */
    fun onAllRefreshables(method: Refreshable.() -> Unit) =
        refreshables.forEach {
            it.method()
        }
}