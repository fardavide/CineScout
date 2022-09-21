package cinescout.design.theme

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin

@Composable
fun Modifier.imageBackground() =
    background(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))

@Composable
fun defaultShimmer(
    baseColor: Color = MaterialTheme.colorScheme.surface,
    highlightColor: Color = MaterialTheme.colorScheme.onSurface,
    width: Dp? = null,
    intensity: Float = ShimmerDefaults.Intensity,
    dropOff: Float = ShimmerDefaults.DropOff,
    tilt: Float = ShimmerDefaults.ShimmerTilt,
    durationMillis: Int = ShimmerDefaults.DurationMillis
) = ShimmerPlugin(
    baseColor = baseColor,
    highlightColor = highlightColor,
    width = width,
    intensity = intensity,
    dropOff = dropOff,
    tilt = tilt,
    durationMillis = durationMillis
)

object ShimmerDefaults {

    /** A definition of the default intensity. */
    const val Intensity = 0.8f

    /** A definition of the default dropOff. */
    const val DropOff = 0.5f

    /** A definition of the default tilt. */
    const val ShimmerTilt = 20f

    /** A definition of the default duration. */
    const val DurationMillis = 650
}
