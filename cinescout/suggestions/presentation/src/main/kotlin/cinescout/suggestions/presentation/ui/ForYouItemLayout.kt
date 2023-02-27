package cinescout.suggestions.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.utils.compose.Adaptive
import cinescout.utils.compose.WindowHeightSizeClass
import cinescout.utils.compose.WindowWidthSizeClass

@Composable
internal fun ForYouItemLayout(
    backdrop: @Composable () -> Unit,
    poster: @Composable () -> Unit,
    infoBox: @Composable () -> Unit,
    genres: @Composable () -> Unit,
    actors: @Composable () -> Unit,
    openDetailsButton: @Composable () -> Unit,
    bookmarkButton: @Composable () -> Unit
) {

    val params = ForYouItemLayout.Params(
        backdrop = backdrop,
        poster = poster,
        infoBox = infoBox,
        genres = genres,
        actors = actors,
        openDetailsButton = openDetailsButton,
        bookmarkButton = bookmarkButton
    )
    Adaptive { windowSizeClass ->
        when (windowSizeClass.width) {
            WindowWidthSizeClass.Compact -> ForYouItemLayout.Vertical(params, spacing = Dimens.Margin.Medium)
            WindowWidthSizeClass.Medium -> ForYouItemLayout.Vertical(params, spacing = Dimens.Margin.Large)
            WindowWidthSizeClass.Expanded -> when (windowSizeClass.height) {
                WindowHeightSizeClass.Compact,
                WindowHeightSizeClass.Medium -> ForYouItemLayout.Horizontal(params)
                WindowHeightSizeClass.Expanded -> ForYouItemLayout.Vertical(params, spacing = Dimens.Margin.XLarge)
            }
        }
    }
}

object ForYouItemLayout {

    @Composable
    internal fun Vertical(params: Params, spacing: Dp) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (
                backdropRef,
                posterRef,
                infoBoxRef,
                genresRef, actorsRef,
                openDetailsButton,
                bookmarkButton
            ) = createRefs()

            Box(
                modifier = Modifier.constrainAs(backdropRef) {
                    height = Dimension.percent(0.35f)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
            ) { params.backdrop() }

            Box(
                modifier = Modifier.constrainAs(posterRef) {
                    width = Dimension.percent(0.3f)
                    height = Dimension.ratio("1:1.5")
                    start.linkTo(parent.start, margin = spacing)
                    top.linkTo(backdropRef.bottom)
                    bottom.linkTo(backdropRef.bottom)
                }
            ) { params.poster() }

            Box(
                modifier = Modifier.constrainAs(infoBoxRef) {
                    width = Dimension.fillToConstraints
                    top.linkTo(backdropRef.bottom, margin = spacing)
                    start.linkTo(posterRef.end, margin = spacing)
                    end.linkTo(parent.end, margin = spacing)
                }
            ) { params.infoBox() }

            val genresTopBarrier = createBottomBarrier(posterRef, infoBoxRef)

            Box(
                modifier = Modifier.constrainAs(genresRef) {
                    width = Dimension.fillToConstraints
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(genresTopBarrier, margin = spacing)
                    bottom.linkTo(actorsRef.top)
                },
                contentAlignment = Alignment.Center
            ) { params.genres() }

            Box(
                modifier = Modifier.constrainAs(actorsRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(genresRef.bottom, margin = spacing)
                    bottom.linkTo(openDetailsButton.top)
                }
            ) { params.actors() }

            Box(
                modifier = Modifier.constrainAs(openDetailsButton) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(actorsRef.bottom, margin = spacing)
                    bottom.linkTo(parent.bottom)
                }
            ) { params.openDetailsButton() }

            Box(
                modifier = Modifier
                    .constrainAs(bookmarkButton) {
                        top.linkTo(parent.top)
                        linkTo(
                            start = parent.start,
                            end = parent.end,
                            bias = 0.9f,
                            endMargin = spacing
                        )
                    }
                    .offset(y = Dimens.ForYouBookmarkButtonOffset)
            ) { params.bookmarkButton() }
        }
    }

    @Composable
    internal fun Horizontal(params: Params) {
        val horizontalSpacing = Dimens.Margin.XLarge
        val verticalSpacing = Dimens.Margin.Medium
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (
                backdropRef,
                posterRef,
                infoBoxRef,
                genresRef,
                actorsRef,
                openDetailsButton,
                bookmarkButton
            ) = createRefs()

            Box(
                modifier = Modifier.constrainAs(backdropRef) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            ) { params.backdrop() }

            val posterBottomGuideline = createGuidelineFromTop(0.5f)
            val posterEndGuideLine = createGuidelineFromStart(0.5f)

            Box(
                modifier = Modifier.constrainAs(posterRef) {
                    height = Dimension.fillToConstraints
                    width = Dimension.ratio("1:1.5")
                    linkTo(start = parent.start, end = posterEndGuideLine, bias = 0f, startMargin = horizontalSpacing)
                    top.linkTo(parent.top, margin = verticalSpacing)
                    bottom.linkTo(posterBottomGuideline)
                }
            ) { params.poster() }

            Box(
                modifier = Modifier.constrainAs(infoBoxRef) {
                    width = Dimension.fillToConstraints
                    start.linkTo(posterRef.end, margin = horizontalSpacing)
                    end.linkTo(parent.end, margin = horizontalSpacing)
                    top.linkTo(posterRef.top, margin = verticalSpacing)
                }
            ) { params.infoBox() }

            Box(
                modifier = Modifier.constrainAs(genresRef) {
                    width = Dimension.fillToConstraints
                    start.linkTo(posterRef.end, margin = horizontalSpacing)
                    end.linkTo(parent.end, margin = horizontalSpacing)
                    top.linkTo(infoBoxRef.bottom, margin = verticalSpacing)
                }
            ) { params.genres() }

            val actorsTopBarrier = createBottomBarrier(posterRef, genresRef)

            Box(
                modifier = Modifier.constrainAs(actorsRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    linkTo(
                        top = actorsTopBarrier,
                        bottom = parent.bottom,
                        bias = 0f,
                        topMargin = verticalSpacing,
                        bottomMargin = verticalSpacing
                    )
                }
            ) { params.actors() }

            Box(
                modifier = Modifier.constrainAs(openDetailsButton) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    linkTo(
                        top = actorsRef.bottom,
                        bottom = parent.bottom,
                        bias = 0f,
                        topMargin = verticalSpacing,
                        bottomMargin = verticalSpacing
                    )
                }
            ) { params.openDetailsButton() }

            Box(
                modifier = Modifier
                    .constrainAs(bookmarkButton) {
                        top.linkTo(parent.top)
                        linkTo(
                            start = parent.start,
                            end = parent.end,
                            bias = 0.9f
                        )
                    }
                    .offset(y = Dimens.ForYouBookmarkButtonOffset)

            ) { params.bookmarkButton() }
        }
    }

    data class Params(
        val actors: @Composable () -> Unit,
        val backdrop: @Composable () -> Unit,
        val bookmarkButton: @Composable () -> Unit,
        val genres: @Composable () -> Unit,
        val infoBox: @Composable () -> Unit,
        val openDetailsButton: @Composable () -> Unit,
        val poster: @Composable () -> Unit
    )
}

@Composable
@Preview(device = Devices.DEFAULT)
@Preview(device = Devices.FOLDABLE)
@Preview(device = Devices.TABLET)
@Preview(widthDp = 900, heightDp = 1500)
private fun ForYouItemLayoutPreview() {
    CineScoutTheme {
        ForYouItemLayout(
            backdrop = {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red),
                    text = "backdrop"
                )
            },
            poster = {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Yellow),
                    text = "poster"
                )
            },
            infoBox = {
                Text(
                    modifier = Modifier
                        .height(96.dp)
                        .fillMaxWidth()
                        .background(Color.Cyan),
                    text = "info box"
                )
            },
            genres = {
                Text(
                    modifier = Modifier
                        .height(72.dp)
                        .background(Color.Blue),
                    text = "genres"
                )
            },
            actors = {
                Text(
                    modifier = Modifier
                        .height(72.dp)
                        .fillMaxWidth()
                        .background(Color.Green),
                    text = "actors"
                )
            },
            openDetailsButton = {
                Text(
                    modifier = Modifier
                        .height(72.dp)
                        .fillMaxWidth()
                        .background(Color.Magenta),
                    text = "buttons"
                )
            },
            bookmarkButton = {
                Text(
                    modifier = Modifier
                        .size(72.dp)
                        .background(Color.Gray),
                    text = "X"
                )
            }
        )
    }
}
