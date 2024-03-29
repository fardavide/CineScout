package cinescout.details.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.Modal
import cinescout.details.presentation.sample.ScreenplayDetailsUiModelSample
import cinescout.resources.R.string
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import kotlin.math.roundToInt

@Composable
internal fun RateItemModal(
    itemTitle: String,
    itemPersonalRating: Int,
    actions: RateItemModal.Actions
) {
    var ratingValue by remember {
        mutableIntStateOf(itemPersonalRating)
    }
    Modal(onDismiss = actions.dismiss) {
        Column(
            modifier = Modifier.padding(vertical = Dimens.Margin.small, horizontal = Dimens.Margin.small),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(string.details_rate_item, itemTitle),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.size(Dimens.Margin.small))
            Text(text = ratingValue.toString(), style = MaterialTheme.typography.displaySmall)
            Spacer(modifier = Modifier.size(Dimens.Margin.small))
            RatingSlider(ratingValue = ratingValue, onRatingChange = { ratingValue = it })
            Spacer(modifier = Modifier.size(Dimens.Margin.small))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = {
                        actions.saveRating(Rating.of(ratingValue).getOrThrow())
                        actions.dismiss()
                    }
                ) {
                    Text(text = stringResource(string.details_rate_item_save))
                }
            }
        }
    }
}

@Composable
private fun RatingSlider(ratingValue: Int, onRatingChange: (Int) -> Unit) {
    val highlightedIconColor = MaterialTheme.colorScheme.primary
    val iconPainter = rememberVectorPainter(image = Icons.Default.Star)
    val iconSize = Dimens.Icon.small
    val canvasWidth = iconSize * 10
    Box {
        Canvas(modifier = Modifier.size(width = canvasWidth, height = iconSize)) {
            repeat(10) { index ->
                with(iconPainter) {
                    if (index >= ratingValue) {
                        draw(iconPainter.intrinsicSize, alpha = 0.35f)
                    } else {
                        draw(iconPainter.intrinsicSize, colorFilter = ColorFilter.tint(highlightedIconColor))
                    }
                }
                drawContext.transform.translate(left = iconSize.toPx())
            }
        }
        Slider(
            modifier = Modifier
                .testTag(TestTag.RateItemSlider)
                .size(width = canvasWidth, height = iconSize)
                .alpha(0f),
            value = ratingValue.toFloat(),
            valueRange = 0f..10f,
            steps = 11,
            onValueChange = { value -> onRatingChange(value.roundToInt()) }
        )
    }
}

object RateItemModal {

    data class Actions(
        val dismiss: () -> Unit,
        val saveRating: (Rating) -> Unit
    ) {

        companion object {

            val Empty = Actions(
                dismiss = {},
                saveRating = {}
            )
        }
    }
}

@Preview
@Composable
private fun RateItemModalPreview() {
    CineScoutTheme {
        RateItemModal(
            itemTitle = ScreenplayDetailsUiModelSample.Inception.title,
            itemPersonalRating = ScreenplayDetailsUiModelSample.Inception.personalRating ?: 0,
            actions = RateItemModal.Actions.Empty
        )
    }
}
