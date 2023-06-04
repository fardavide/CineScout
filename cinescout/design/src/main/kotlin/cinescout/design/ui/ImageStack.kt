package cinescout.design.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
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
    properties: ImageStack.Properties = ImageStack.Properties.smallImage()
) {
    val measurePolicy = overlappingRowMeasurePolicy(properties.overlapFactor)
    val content = @Composable {
        for ((index, imageModel) in imageModels.withIndex()) {
            Box(
                modifier = Modifier
                    .border(width = properties.borderSize, color = properties.borderColor, shape = CircleShape)
                    .padding(1.dp)
                    .clip(CircleShape)
                    .zIndex(imageModels.size - index.toFloat())
            ) {
                CoilImage(
                    modifier = Modifier
                        .size(properties.imageSize)
                        .imageBackground(),
                    imageModel = { imageModel },
                    failure = { FailureImage() },
                    loading = { CenteredProgress() },
                    previewPlaceholder = drawable.img_poster
                )
            }
        }
    }
    if (imageModels.isNotEmpty()) {
        Layout(measurePolicy = measurePolicy, modifier = modifier, content = content)
    }
}

private fun overlappingRowMeasurePolicy(overlapFactor: Float) = MeasurePolicy { measurables, constraints ->
    val placeables = measurables.map { measurable -> measurable.measure(constraints) }
    val height = placeables.maxOfOrNull { it.height } ?: 0
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

            @Composable
            fun smallImage(
                borderColor: Color = MaterialTheme.colorScheme.surfaceVariant,
                borderSize: Dp = Dimens.Margin.XSmall,
                overlapFactor: Float = 0.7f
            ) = Properties(
                borderColor = borderColor,
                borderSize = borderSize,
                imageSize = Dimens.Image.Small,
                overlapFactor = overlapFactor
            )

            @Composable
            fun mediumImage(
                borderColor: Color = MaterialTheme.colorScheme.surfaceVariant,
                borderSize: Dp = Dimens.Margin.XSmall,
                overlapFactor: Float = 0.7f
            ) = Properties(
                borderColor = borderColor,
                borderSize = borderSize,
                imageSize = Dimens.Image.Medium,
                overlapFactor = overlapFactor
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

@Preview
@Composable
fun EmptyImageStackPreview() {
    CineScoutTheme {
        ImageStack(imageModels = persistentListOf())
    }
}
