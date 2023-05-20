package cinescout.design.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.resources.R.drawable
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ImageStack(
    imageModels: ImmutableList<Any>,
    modifier: Modifier = Modifier,
    properties: ImageStack.Properties = ImageStack.Properties.Default
) {
    val measurePolicy = overlappingRowMeasurePolicy(properties.overlapFactor)
    val content = @Composable {
        for ((index, imageModel) in imageModels.withIndex()) {
            CoilImage(
                modifier = Modifier
                    .size(properties.imageSize)
                    .clip(CircleShape)
                    .imageBackground()
                    .border(width = properties.borderSize, color = properties.borderColor, shape = CircleShape)
                    .zIndex(imageModels.size - index.toFloat()),
                imageModel = { imageModel },
                failure = { FailureImage() },
                loading = { CenteredProgress() },
                previewPlaceholder = drawable.img_poster
            )
        }
    }
    Layout(measurePolicy = measurePolicy, modifier = modifier, content = content)
}

private fun overlappingRowMeasurePolicy(overlapFactor: Float) = MeasurePolicy { measurables, constraints ->
    val placeables = measurables.map { measurable -> measurable.measure(constraints) }
    val height = placeables.maxOf { it.height }
    val width = placeables.subList(1, placeables.size).sumOf { it.width } * overlapFactor + placeables[0].width
    layout(width.toInt(), height) {
        var xPos = 0
        for (placeable in placeables) {
            placeable.placeRelative(xPos, 0, 0f)
            xPos += (placeable.width * overlapFactor).toInt()
        }
    }
}

object ImageStack {

    data class Properties(
        val borderColor: Color,
        val borderSize: Dp,
        val imageSize: Dp,
        val overlapFactor: Float
    ) {

        companion object {

            val Default @Composable get() = Properties(
                borderColor = MaterialTheme.colorScheme.surfaceVariant,
                borderSize = Dimens.Margin.XSmall,
                imageSize = Dimens.Image.Small,
                overlapFactor = 0.7f
            )
        }
    }
}

@Preview
@Composable
fun ImageStackPreview() {
    CineScoutTheme {
        ImageStack(imageModels = persistentListOf(1, 2, 3, 4, 5))
    }
}
