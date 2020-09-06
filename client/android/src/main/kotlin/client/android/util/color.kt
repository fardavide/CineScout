package client.android.util

import design.Color
import util.Percentage
import util.times
import util.unaryMinus
import androidx.compose.ui.graphics.Color as ComposeColor

/**
 * Blend 2 [Color]s
 * @param balance defines how dominant should be [other] over the receiver [Color]
 *   Example `Red.blend(Blue, 0.percent)` will return `Red`, while `Red.blend(Blue, 100.percent)` will return `Blue`
 */
fun ComposeColor.blend(other: ComposeColor, balance: Percentage): ComposeColor {
    return copy(
        red = red * -balance + other.red * balance,
        green = green * -balance + other.green * balance,
        blue = blue * -balance + other.blue * balance,
    )
}

fun ComposeColor.blend(other: Color, balance: Percentage): ComposeColor {
    return copy(
        red = red * -balance + other.toComposeColor().red * balance,
        green = green * -balance + other.toComposeColor().green * balance,
        blue = blue * -balance + other.toComposeColor().blue * balance,
    )
}

fun Color.toComposeColor() = ComposeColor(hex)

