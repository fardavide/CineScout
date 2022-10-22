package cinescout.suggestions.presentation.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import cinescout.suggestions.presentation.model.ForYouHintAction
import cinescout.suggestions.presentation.sample.ForYouMovieUiModelSample
import cinescout.suggestions.presentation.viewmodel.ForYouHintViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import studio.forface.cinescout.design.R.string

@Composable
fun ForYouHintScreen(onBack: () -> Unit) {
    val viewModel: ForYouHintViewModel = koinViewModel()
    val onDismiss = {
        viewModel.submit(ForYouHintAction.Dismiss)
        onBack()
    }
    ForYouHintScreen(onDismiss = onDismiss)
}

@Composable
fun ForYouHintScreen(onDismiss: () -> Unit, modifier: Modifier = Modifier) {
    val model = ForYouMovieUiModelSample.Inception
    val xOffset = remember(model.tmdbMovieId.value) { Animatable(100f) }
    rememberCoroutineScope().launch {
        val animationSpec = spring<Float>(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow
        )
        while (true) {
            xOffset.animateTo(0f, animationSpec = animationSpec)
            xOffset.animateTo(ForYouMovieItem.DragThreshold.toFloat(), animationSpec = animationSpec)
            xOffset.animateTo(-ForYouMovieItem.DragThreshold.toFloat(), animationSpec = animationSpec)
        }
    }
    Column(
        modifier = modifier
            .navigationBarsPadding()
            .background(MaterialTheme.colorScheme.surfaceTint)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
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
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(Dimens.Margin.Large),
            text = stringResource(id = string.suggestions_for_you_hint),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        TextButton(onClick = onDismiss) {
            Text(
                text = stringResource(id = string.suggestions_for_you_hint_dismiss),
                color = MaterialTheme.colorScheme.inverseOnSurface)
        }
    }
}

@Composable
@Preview(showBackground = true)
@Preview(showSystemUi = true, device = Devices.TABLET)
private fun ForYouHintScreenPreview() {
    CineScoutTheme {
        ForYouHintScreen(onBack = {})
    }
}
