package view

import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.TextVisual
import java.awt.Color

/**
 * Custom [CardStack] for [CardView]s with a fixed width of 130px and a fixed height of 200px.
 * It is used for all game stacks on the table.
 *
 * Automatically has a [CompoundVisual] that combines a white semi-transparent [ColorVisual] with
 * a [TextVisual] showing a label.
 *
 * @param label the label for this stack view; defaults to empty string.
 * @param rotate if true, the label will be rotated 180 degrees
 */
class LabeledStackView(posX: Number, posY: Number, label: String = "", rotate: Boolean = false) :
    CardStack<CardView>(height = 200, width = 130, posX = posX, posY = posY) {

    init {
        visual = CompoundVisual(
            ColorVisual(Color(255, 255, 255, 50)),
            TextVisual(label)
        ).apply {
            if (rotate) rotation = 180.0
        }
    }

}