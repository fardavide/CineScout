package cinescout.lists.presentation.ui

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
import cinescout.lists.presentation.model.ListType
import cinescout.lists.presentation.previewdata.ListTypeSelectorPreviewProvider

@Composable
internal fun ListTypeSelector(
    type: ListType,
    onTypeSelected: (ListType) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedType by remember { mutableStateOf(type) }

    val baseConstraintSet = ConstraintSet {
        val moviesRef = createRefFor(ListTypeSelector.LayoutId.Movies)
        val allRef = createRefFor(ListTypeSelector.LayoutId.All)
        val tvShowsRef = createRefFor(ListTypeSelector.LayoutId.TvShows)

        constrain(moviesRef) {
            width = Dimension.preferredWrapContent
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(allRef.start)
        }
        constrain(allRef) {
            width = Dimension.preferredWrapContent
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(moviesRef.end)
            end.linkTo(tvShowsRef.start)
        }
        constrain(tvShowsRef) {
            width = Dimension.preferredWrapContent
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(allRef.end)
            end.linkTo(parent.end)
        }

    }
    val moviesSelectedConstraintSet = ConstraintSet(baseConstraintSet) {
        val moviesRef = createRefFor(ListTypeSelector.LayoutId.Movies)
        val selectorRef = createRefFor(ListTypeSelector.LayoutId.Selector)

        constrain(selectorRef) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            linkTo(start = moviesRef.start, end = moviesRef.end)
        }
    }
    val allSelectedConstraintSet = ConstraintSet(baseConstraintSet) {
        val allRef = createRefFor(ListTypeSelector.LayoutId.All)
        val selectorRef = createRefFor(ListTypeSelector.LayoutId.Selector)

        constrain(selectorRef) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            linkTo(start = allRef.start, end = allRef.end)
        }
    }
    val tvShowsSelectedConstraintSet = ConstraintSet(baseConstraintSet) {
        val tvShowsRef = createRefFor(ListTypeSelector.LayoutId.TvShows)
        val selectorRef = createRefFor(ListTypeSelector.LayoutId.Selector)

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
            ListType.Movies -> moviesSelectedConstraintSet
            ListType.All -> allSelectedConstraintSet
            ListType.TvShows -> tvShowsSelectedConstraintSet
        },
        animateChanges = ListTypeSelector.animateChanges,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
    ) {
        val height = Dimens.Icon.Medium
        val contentPadding = PaddingValues(Dimens.Margin.Small)
        val selectedContentColor = MaterialTheme.colorScheme.onPrimary
        val unselectedContentColor = MaterialTheme.colorScheme.onSurface
        val colorChangeAnimationSpec = tween<Color>(durationMillis = 200, delayMillis = 50)
        val moviesContentColor = animateColorAsState(
            targetValue = if (selectedType == ListType.Movies) selectedContentColor else unselectedContentColor,
            animationSpec = colorChangeAnimationSpec
        )
        val allContentColor = animateColorAsState(
            targetValue = if (selectedType == ListType.All) selectedContentColor else unselectedContentColor,
            animationSpec = colorChangeAnimationSpec
        )
        val tvShowsContentColor = animateColorAsState(
            targetValue = if (selectedType == ListType.TvShows) selectedContentColor else unselectedContentColor,
            animationSpec = colorChangeAnimationSpec
        )
        Box(
            modifier = Modifier
                .layoutId(ListTypeSelector.LayoutId.Selector)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
        )
        Button(
            modifier = Modifier
                .layoutId(ListTypeSelector.LayoutId.Movies)
                .height(height)
                .selectable(selected = selectedType == ListType.Movies, onClick = {}),
            contentPadding = contentPadding,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = moviesContentColor.value
            ),
            onClick = {
                selectedType = ListType.Movies
                onTypeSelected(ListType.Movies)
            }
        ) {
            Text(text = stringResource(id = string.item_type_movies))
        }
        Button(
            modifier = Modifier
                .layoutId(ListTypeSelector.LayoutId.All)
                .height(height)
                .selectable(selected = selectedType == ListType.All, onClick = {}),
            contentPadding = contentPadding,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = allContentColor.value
            ),
            onClick = {
                selectedType = ListType.All
                onTypeSelected(ListType.All)
            }
        ) {
            Text(text = stringResource(id = string.item_type_all))
        }
        Button(
            modifier = Modifier
                .layoutId(ListTypeSelector.LayoutId.TvShows)
                .height(height)
                .selectable(selected = selectedType == ListType.TvShows, onClick = {}),
            contentPadding = contentPadding,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = tvShowsContentColor.value
            ),
            onClick = {
                selectedType = ListType.TvShows
                onTypeSelected(ListType.TvShows)
            }
        ) {
            Text(text = stringResource(id = string.item_type_tv_shows))
        }
    }
}

object ListTypeSelector {

    @set:VisibleForTesting
    var animateChanges = true

    object LayoutId {

        const val All = "all"
        const val Movies = "movies"
        const val Selector = "selector"
        const val TvShows = "tvShows"
    }
}

@Composable
@AdaptivePreviews.WithBackground
@Preview(locale = "it")
private fun ListTypeSelectorPreview(
    @PreviewParameter(ListTypeSelectorPreviewProvider::class) type: ListType
) {
    CineScoutTheme {
        ListTypeSelector(type, onTypeSelected = {})
    }
}
