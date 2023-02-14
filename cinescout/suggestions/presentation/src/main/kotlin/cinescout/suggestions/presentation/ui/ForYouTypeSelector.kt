package cinescout.suggestions.presentation.ui

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import cinescout.design.AdaptivePreviews
import cinescout.design.R.string
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.previewdata.ForYouTypeSelectorPreviewProvider

@Composable
internal fun ForYouTypeSelector(
    type: ForYouType,
    onTypeSelected: (ForYouType) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedType by remember { mutableStateOf(type) }

    val baseConstraintSet = ConstraintSet {
        val moviesRef = createRefFor(ForYouTypeSelector.LayoutId.Movies)
        val tvShowsRef = createRefFor(ForYouTypeSelector.LayoutId.TvShows)

        constrain(moviesRef) {
            width = Dimension.preferredWrapContent
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(tvShowsRef.start)
        }
        constrain(tvShowsRef) {
            width = Dimension.preferredWrapContent
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(moviesRef.end)
            end.linkTo(parent.end)
        }

    }
    val moviesSelectedConstraintSet = ConstraintSet(baseConstraintSet) {
        val moviesRef = createRefFor(ForYouTypeSelector.LayoutId.Movies)
        val selectorRef = createRefFor(ForYouTypeSelector.LayoutId.Selector)

        constrain(selectorRef) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            linkTo(start = moviesRef.start, end = moviesRef.end)
        }
    }
    val tvShowsSelectedConstraintSet = ConstraintSet(baseConstraintSet) {
        val tvShowsRef = createRefFor(ForYouTypeSelector.LayoutId.TvShows)
        val selectorRef = createRefFor(ForYouTypeSelector.LayoutId.Selector)

        constrain(selectorRef) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            linkTo(start = tvShowsRef.start, end = tvShowsRef.end)
        }
    }
    ConstraintLayout(
        modifier = modifier
            .border(
                width = Dimens.Outline,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.small
            )
            .padding(Dimens.Outline),
        constraintSet = when (selectedType) {
            ForYouType.Movies -> moviesSelectedConstraintSet
            ForYouType.TvShows -> tvShowsSelectedConstraintSet
        },
        animateChanges = ForYouTypeSelector.animateChanges,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
    ) {
        val height = Dimens.Icon.Medium
        val contentPadding = PaddingValues(Dimens.Margin.Small)
        val selectedContentColor = MaterialTheme.colorScheme.onPrimary
        val unselectedContentColor = MaterialTheme.colorScheme.onSurface
        val colorChangeAnimationSpec = tween<Color>(durationMillis = 200, delayMillis = 50)
        val moviesContentColor = animateColorAsState(
            targetValue = if (selectedType == ForYouType.Movies) selectedContentColor else unselectedContentColor,
            animationSpec = colorChangeAnimationSpec
        )
        val tvShowsContentColor = animateColorAsState(
            targetValue = if (selectedType == ForYouType.TvShows) selectedContentColor else unselectedContentColor,
            animationSpec = colorChangeAnimationSpec
        )
        Box(
            modifier = Modifier
                .layoutId(ForYouTypeSelector.LayoutId.Selector)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
        )
        Button(
            modifier = Modifier
                .layoutId(ForYouTypeSelector.LayoutId.Movies)
                .height(height)
                .selectable(selected = selectedType == ForYouType.Movies, onClick = {}),
            contentPadding = contentPadding,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = moviesContentColor.value
            ),
            onClick = {
                selectedType = ForYouType.Movies
                onTypeSelected(ForYouType.Movies)
            }
        ) {
            Text(text = stringResource(id = string.item_type_movies))
        }
        Button(
            modifier = Modifier
                .layoutId(ForYouTypeSelector.LayoutId.TvShows)
                .height(height)
                .selectable(selected = selectedType == ForYouType.TvShows, onClick = {}),
            contentPadding = contentPadding,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = tvShowsContentColor.value
            ),
            onClick = {
                selectedType = ForYouType.TvShows
                onTypeSelected(ForYouType.TvShows)
            }
        ) {
            Text(text = stringResource(id = string.item_type_tv_shows))
        }
    }
}

object ForYouTypeSelector {

    @set:VisibleForTesting
    var animateChanges = true

    object LayoutId {

        const val Movies = "movies"
        const val Selector = "selector"
        const val TvShows = "tvShows"
    }
}

@Composable
@AdaptivePreviews.WithBackground
@Preview(locale = "it")
private fun ForYouTypeSelectorPreview(
    @PreviewParameter(ForYouTypeSelectorPreviewProvider::class) type: ForYouType
) {
    CineScoutTheme {
        ForYouTypeSelector(type, onTypeSelected = {})
    }
}
