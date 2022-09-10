package cinescout.suggestions.presentation.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.suggestions.presentation.previewdata.ForYouMovieUiModelPreviewData
import kotlinx.coroutines.launch
import studio.forface.cinescout.design.R.string

@Composable
fun ForYouHintScreen() {
    val model = ForYouMovieUiModelPreviewData.Inception
    val xOffset = remember(model.tmdbMovieId.value) { Animatable(100f) }
    rememberCoroutineScope().launch {
        val animationSpec = spring<Float>(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow
        )
        while (true) {
            xOffset.animateTo(ForYouMovieItem.DragThreshold.toFloat(), animationSpec = animationSpec)
            xOffset.animateTo(-ForYouMovieItem.DragThreshold.toFloat(), animationSpec = animationSpec)
        }
    }
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceTint)
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.75f)
                .padding(start = Dimens.Margin.XLarge, end = Dimens.Margin.XLarge, top = Dimens.Margin.XLarge)
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.extraLarge)
        ) {
            ForYouMovieItem(
                modifier = Modifier
                    .scale(0.9f)
                    .aspectRatio(0.6f),
                model = model,
                actions = ForYouMovieItem.Actions.Empty,
                xOffset = xOffset
            )
        }
        Box(modifier = Modifier.fillMaxSize().padding(Dimens.Margin.Large), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(id = string.suggestions_for_you_hint),
                color = MaterialTheme.colorScheme.inverseOnSurface,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
@Preview(showSystemUi = true, device = Devices.TABLET)
private fun ForYouHintScreenPreview() {
    CineScoutTheme {
        ForYouHintScreen()
    }
}
