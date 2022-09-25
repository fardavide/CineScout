package cinescout.suggestions.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.util.Adaptive
import cinescout.design.util.WindowWidthSizeClass

@Composable
internal fun ForYouMovieItemLayout(
    backdrop: @Composable () -> Unit,
    poster: @Composable () -> Unit,
    infoBox: @Composable () -> Unit,
    genres: @Composable () -> Unit,
    actors: @Composable () -> Unit,
    buttons: @Composable RowScope.() -> Unit,
    overlay: @Composable () -> Unit
) {

    Adaptive { windowSizeClass ->
        when (windowSizeClass.width) {
            WindowWidthSizeClass.Compact -> ForYouMovieItemLayout.Compact(
                backdrop = backdrop,
                poster = poster,
                infoBox = infoBox,
                genres = genres,
                actors = actors,
                buttons = buttons,
                overlay = overlay
            )
            WindowWidthSizeClass.Medium -> ForYouMovieItemLayout.Medium(
                backdrop = backdrop,
                poster = poster,
                infoBox = infoBox,
                genres = genres,
                actors = actors,
                buttons = buttons,
                overlay = overlay
            )
            WindowWidthSizeClass.Expanded -> ForYouMovieItemLayout.Expanded(
                backdrop = backdrop,
                poster = poster,
                infoBox = infoBox,
                genres = genres,
                actors = actors,
                buttons = buttons,
                overlay = overlay
            )
        }
    }
}

object ForYouMovieItemLayout {

    @Composable
    internal fun Compact(
        backdrop: @Composable () -> Unit,
        poster: @Composable () -> Unit,
        infoBox: @Composable () -> Unit,
        genres: @Composable () -> Unit,
        actors: @Composable () -> Unit,
        buttons: @Composable RowScope.() -> Unit,
        overlay: @Composable () -> Unit
    ) {
        val spacing = Dimens.Margin.Medium
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (backdropRef, posterRef, infoBoxRef, genresRef, actorsRef, buttonsRef, overlayRef) = createRefs()

            Box(
                modifier = Modifier.constrainAs(backdropRef) {
                    height = Dimension.percent(0.35f)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
            ) { backdrop() }

            Box(
                modifier = Modifier.constrainAs(posterRef) {
                    width = Dimension.percent(0.3f)
                    height = Dimension.ratio("1:1.5")
                    start.linkTo(parent.start, margin = spacing)
                    top.linkTo(backdropRef.bottom)
                    bottom.linkTo(backdropRef.bottom)
                }
            ) { poster() }

            Box(
                modifier = Modifier.constrainAs(infoBoxRef) {
                    width = Dimension.fillToConstraints
                    top.linkTo(backdropRef.bottom, margin = spacing)
                    start.linkTo(posterRef.end, margin = spacing)
                    end.linkTo(parent.end, margin = spacing)
                }
            ) { infoBox() }

            val genresTopBarrier = createBottomBarrier(posterRef, infoBoxRef)

            Box(
                modifier = Modifier.constrainAs(genresRef) {
                    width = Dimension.fillToConstraints
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(genresTopBarrier, margin = spacing)
                    bottom.linkTo(actorsRef.top)
                }
            ) { genres() }

            Box(
                modifier = Modifier.constrainAs(actorsRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(genresRef.bottom, margin = spacing)
                }
            ) { actors() }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(buttonsRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end, margin = spacing)
                        bottom.linkTo(parent.bottom, margin = spacing)
                    }
            ) { buttons() }

            Box(
                modifier = Modifier.constrainAs(overlayRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            ) { overlay() }
        }
    }

    @Composable
    internal fun Medium(
        backdrop: @Composable () -> Unit,
        poster: @Composable () -> Unit,
        infoBox: @Composable () -> Unit,
        genres: @Composable () -> Unit,
        actors: @Composable () -> Unit,
        buttons: @Composable RowScope.() -> Unit,
        overlay: @Composable () -> Unit
    ) {
        val spacing = Dimens.Margin.Large
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (backdropRef, posterRef, infoBoxRef, genresRef, actorsRef, buttonsRef, overlayRef) = createRefs()

            Box(
                modifier = Modifier.constrainAs(backdropRef) {
                    height = Dimension.percent(0.38f)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
            ) { backdrop() }

            Box(
                modifier = Modifier.constrainAs(posterRef) {
                    width = Dimension.percent(0.2f)
                    height = Dimension.ratio("1:1.5")
                    start.linkTo(parent.start, margin = spacing)
                    top.linkTo(backdropRef.bottom)
                    bottom.linkTo(backdropRef.bottom)
                }
            ) { poster() }

            Box(
                modifier = Modifier.constrainAs(infoBoxRef) {
                    width = Dimension.fillToConstraints
                    top.linkTo(backdropRef.bottom, margin = spacing)
                    start.linkTo(posterRef.end, margin = spacing)
                    end.linkTo(parent.end, margin = spacing)
                }
            ) { infoBox() }

            val genresTopBarrier = createBottomBarrier(posterRef, infoBoxRef)

            Box(
                modifier = Modifier.constrainAs(genresRef) {
                    width = Dimension.fillToConstraints
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(genresTopBarrier, margin = spacing)
                    bottom.linkTo(actorsRef.top)
                }
            ) { genres() }

            Box(
                modifier = Modifier.constrainAs(actorsRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(genresRef.bottom, margin = spacing)
                }
            ) { actors() }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(buttonsRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end, margin = spacing)
                        bottom.linkTo(parent.bottom, margin = spacing)
                    }
            ) { buttons() }

            Box(
                modifier = Modifier.constrainAs(overlayRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            ) { overlay() }
        }
    }

    @Composable
    internal fun Expanded(
        backdrop: @Composable () -> Unit,
        poster: @Composable () -> Unit,
        infoBox: @Composable () -> Unit,
        genres: @Composable () -> Unit,
        actors: @Composable () -> Unit,
        buttons: @Composable RowScope.() -> Unit,
        overlay: @Composable () -> Unit
    ) {
        val horizontalSpacing = Dimens.Margin.XLarge
        val verticalSpacing = Dimens.Margin.Medium
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (
                backdropRef,
                posterRef,
                infoBoxRef,
                genresRef,
                actorsRef,
                buttonsRef,
                overlayRef
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
            ) { backdrop() }

            val posterBottomGuideline = createGuidelineFromTop(0.5f)
            val posterEndGuideLine = createGuidelineFromStart(0.7f)

            Box(
                modifier = Modifier.constrainAs(posterRef) {
                    height = Dimension.fillToConstraints
                    width = Dimension.ratio("1:1.5")
                    start.linkTo(parent.start, margin = horizontalSpacing)
                    end.linkTo(posterEndGuideLine)
                    top.linkTo(parent.top, margin = verticalSpacing)
                    bottom.linkTo(posterBottomGuideline)
                }
            ) { poster() }

            Box(
                modifier = Modifier.constrainAs(infoBoxRef) {
                    width = Dimension.fillToConstraints
                    start.linkTo(posterRef.end, margin = horizontalSpacing)
                    end.linkTo(parent.end, margin = horizontalSpacing)
                    top.linkTo(posterRef.top, margin = verticalSpacing)
                }
            ) { infoBox() }

            Box(
                modifier = Modifier.constrainAs(genresRef) {
                    width = Dimension.fillToConstraints
                    start.linkTo(posterRef.end, margin = horizontalSpacing)
                    end.linkTo(parent.end, margin = horizontalSpacing)
                    top.linkTo(infoBoxRef.bottom, margin = verticalSpacing)
                }
            ) { genres() }

            val actorsTopBarrier = createBottomBarrier(posterRef, genresRef)

            Box(
                modifier = Modifier.constrainAs(actorsRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(actorsTopBarrier, margin = verticalSpacing)
                    bottom.linkTo(buttonsRef.top, margin = verticalSpacing)
                }
            ) { actors() }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(buttonsRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end, margin = horizontalSpacing)
                        bottom.linkTo(parent.bottom, margin = verticalSpacing)
                    }
            ) { buttons() }

            Box(
                modifier = Modifier.constrainAs(overlayRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            ) { overlay() }
        }
    }
}

@Composable
@Preview(device = Devices.DEFAULT)
@Preview(device = Devices.FOLDABLE)
@Preview(device = Devices.TABLET)
@Preview(widthDp = 900, heightDp = 1500)
private fun ForYouMovieItemLayoutPreview() {
    CineScoutTheme {
        ForYouMovieItemLayout(
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
            buttons = {
                Text(
                    modifier = Modifier
                        .height(72.dp)
                        .fillMaxWidth()
                        .background(Color.Magenta),
                    text = "buttons"
                )
            },
            overlay = { }
        )
    }
}
