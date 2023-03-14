package cinescout.details.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import cinescout.utils.compose.WindowSizeClass
import cinescout.utils.compose.WindowWidthSizeClass

@Composable
internal fun ScreenplayDetailsLayout(
    backdrops: @Composable () -> Unit,
    poster: @Composable () -> Unit,
    infoBox: @Composable () -> Unit,
    ratings: @Composable RowScope.() -> Unit,
    genres: @Composable () -> Unit,
    credits: @Composable () -> Unit,
    overview: @Composable () -> Unit,
    videos: @Composable () -> Unit
) {
    Adaptive { windowSizeClass ->
        when (windowSizeClass.width) {
            WindowWidthSizeClass.Compact -> ScreenplayDetailsLayout.Vertical(
                spacing = Dimens.Margin.Medium,
                backdrops = backdrops,
                poster = poster,
                infoBox = infoBox,
                ratings = ratings,
                genres = genres,
                credits = credits,
                overview = overview,
                videos = videos
            )

            WindowWidthSizeClass.Medium -> ScreenplayDetailsLayout.Vertical(
                spacing = Dimens.Margin.Large,
                backdrops = backdrops,
                poster = poster,
                infoBox = infoBox,
                ratings = ratings,
                genres = genres,
                credits = credits,
                overview = overview,
                videos = videos
            )

            WindowWidthSizeClass.Expanded -> when (windowSizeClass.height) {
                WindowHeightSizeClass.Compact,
                WindowHeightSizeClass.Medium -> ScreenplayDetailsLayout.Horizontal(
                    backdrops = backdrops,
                    poster = poster,
                    infoBox = infoBox,
                    ratings = ratings,
                    genres = genres,
                    credits = credits,
                    overview = overview,
                    videos = videos
                )

                WindowHeightSizeClass.Expanded -> ScreenplayDetailsLayout.Vertical(
                    spacing = Dimens.Margin.XLarge,
                    backdrops = backdrops,
                    poster = poster,
                    infoBox = infoBox,
                    ratings = ratings,
                    genres = genres,
                    credits = credits,
                    overview = overview,
                    videos = videos
                )
            }
        }
    }
}

@Composable
internal fun ScreenplayDetailsLayout(
    mode: ScreenplayDetailsLayout.Mode,
    backdrops: @Composable () -> Unit,
    poster: @Composable () -> Unit,
    infoBox: @Composable () -> Unit,
    ratings: @Composable RowScope.() -> Unit,
    genres: @Composable () -> Unit,
    credits: @Composable () -> Unit,
    overview: @Composable () -> Unit,
    videos: @Composable () -> Unit
) {
    when (mode) {
        ScreenplayDetailsLayout.Mode.Horizontal -> ScreenplayDetailsLayout.Horizontal(
            backdrops = backdrops,
            poster = poster,
            infoBox = infoBox,
            ratings = ratings,
            genres = genres,
            credits = credits,
            overview = overview,
            videos = videos
        )
        is ScreenplayDetailsLayout.Mode.Vertical -> ScreenplayDetailsLayout.Vertical(
            spacing = mode.spacing,
            backdrops = backdrops,
            poster = poster,
            infoBox = infoBox,
            ratings = ratings,
            genres = genres,
            credits = credits,
            overview = overview,
            videos = videos
        )
    }
}

object ScreenplayDetailsLayout {

    sealed interface Mode {

        object Horizontal : Mode
        data class Vertical(val spacing: Dp) : Mode

        companion object {

            fun forClass(windowSizeClass: WindowSizeClass): Mode {
                return when (windowSizeClass.width) {
                    WindowWidthSizeClass.Compact -> Vertical(spacing = Dimens.Margin.Medium)
                    WindowWidthSizeClass.Medium -> Vertical(spacing = Dimens.Margin.Large)
                    WindowWidthSizeClass.Expanded -> when (windowSizeClass.height) {
                        WindowHeightSizeClass.Compact,
                        WindowHeightSizeClass.Medium -> Horizontal

                        WindowHeightSizeClass.Expanded -> Vertical(spacing = Dimens.Margin.XLarge)
                    }
                }
            }
        }
    }

    @Composable
    internal fun Vertical(
        spacing: Dp,
        backdrops: @Composable () -> Unit,
        poster: @Composable () -> Unit,
        infoBox: @Composable () -> Unit,
        ratings: @Composable RowScope.() -> Unit,
        genres: @Composable () -> Unit,
        credits: @Composable () -> Unit,
        overview: @Composable () -> Unit,
        videos: @Composable () -> Unit
    ) {
        val scrollState = rememberScrollState()
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            val (
                backdropsRef,
                posterRef,
                infoBoxRef,
                ratingsRef,
                genresRef,
                creditsRef,
                overviewRef,
                videosRef
            ) = createRefs()

            Box(
                modifier = Modifier.constrainAs(backdropsRef) {
                    width = Dimension.fillToConstraints
                    height = Dimension.ratio("3:2")
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) { backdrops() }

            Box(
                modifier = Modifier.constrainAs(posterRef) {
                    width = Dimension.percent(0.3f)
                    height = Dimension.ratio("1:1.5")
                    top.linkTo(backdropsRef.bottom)
                    bottom.linkTo(backdropsRef.bottom)
                    start.linkTo(parent.start, margin = spacing)
                }
            ) { poster() }

            Box(
                modifier = Modifier.constrainAs(infoBoxRef) {
                    width = Dimension.fillToConstraints
                    top.linkTo(backdropsRef.bottom, margin = spacing)
                    start.linkTo(posterRef.end, margin = spacing)
                    end.linkTo(parent.end, margin = spacing)
                }
            ) { infoBox() }

            val barrier = createBottomBarrier(posterRef, infoBoxRef)

            Row(
                modifier = Modifier.constrainAs(ratingsRef) {
                    width = Dimension.fillToConstraints
                    top.linkTo(barrier, margin = spacing)
                    start.linkTo(parent.start, margin = spacing)
                    end.linkTo(parent.end, margin = spacing)
                },
                horizontalArrangement = Arrangement.spacedBy(Dimens.Margin.Medium),
                verticalAlignment = Alignment.CenterVertically
            ) { ratings() }

            Box(
                modifier = Modifier.constrainAs(genresRef) {
                    top.linkTo(ratingsRef.bottom, margin = spacing)
                    start.linkTo(parent.start)
                    bottom.linkTo(creditsRef.top)
                    end.linkTo(parent.end)
                }
            ) { genres() }

            Box(
                modifier = Modifier.constrainAs(creditsRef) {
                    width = Dimension.fillToConstraints
                    top.linkTo(genresRef.bottom, margin = spacing)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) { credits() }

            Box(
                modifier = Modifier.constrainAs(overviewRef) {
                    width = Dimension.fillToConstraints
                    top.linkTo(creditsRef.bottom, margin = spacing)
                    start.linkTo(parent.start, margin = spacing)
                    end.linkTo(parent.end, margin = spacing)
                }
            ) { overview() }

            Box(
                modifier = Modifier.constrainAs(videosRef) {
                    width = Dimension.fillToConstraints
                    top.linkTo(overviewRef.bottom, margin = spacing)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = spacing)
                }
            ) { videos() }
        }
    }

    @Composable
    internal fun Horizontal(
        backdrops: @Composable () -> Unit,
        poster: @Composable () -> Unit,
        infoBox: @Composable () -> Unit,
        ratings: @Composable RowScope.() -> Unit,
        genres: @Composable () -> Unit,
        credits: @Composable () -> Unit,
        overview: @Composable () -> Unit,
        videos: @Composable () -> Unit
    ) {
        val spacing = Dimens.Margin.Medium
        val scrollState = rememberScrollState()
        Row(modifier = Modifier.fillMaxSize()) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth(0.77f)
                    .verticalScroll(scrollState)
            ) {
                val (
                    backdropsRef,
                    posterRef,
                    infoBoxRef,
                    ratingsRef,
                    genresRef,
                    overviewRef,
                    videosRef
                ) = createRefs()

                val startVerticalGuideline = createGuidelineFromStart(0.32f)

                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraLarge)
                        .constrainAs(backdropsRef) {
                            width = Dimension.fillToConstraints
                            height = Dimension.ratio("3:2")
                            top.linkTo(parent.top, margin = spacing)
                            start.linkTo(startVerticalGuideline)
                            end.linkTo(parent.end)
                        }
                ) { backdrops() }

                Box(
                    modifier = Modifier.constrainAs(posterRef) {
                        width = Dimension.fillToConstraints
                        height = Dimension.ratio("1:1.5")
                        top.linkTo(parent.top, margin = spacing)
                        start.linkTo(parent.start, margin = spacing)
                        end.linkTo(startVerticalGuideline, margin = spacing)
                    }
                ) { poster() }

                Box(
                    modifier = Modifier.constrainAs(infoBoxRef) {
                        width = Dimension.fillToConstraints
                        top.linkTo(posterRef.bottom, margin = spacing)
                        start.linkTo(parent.start, margin = spacing)
                        end.linkTo(startVerticalGuideline, margin = spacing)
                    }
                ) { infoBox() }

                Row(
                    modifier = Modifier.constrainAs(ratingsRef) {
                        width = Dimension.fillToConstraints
                        top.linkTo(backdropsRef.bottom, margin = spacing)
                        start.linkTo(startVerticalGuideline, margin = spacing)
                        end.linkTo(parent.end, margin = spacing)
                    },
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Margin.Medium),
                    verticalAlignment = Alignment.CenterVertically
                ) { ratings() }

                Box(
                    modifier = Modifier.constrainAs(genresRef) {
                        width = Dimension.preferredWrapContent
                        top.linkTo(infoBoxRef.bottom, margin = spacing)
                        start.linkTo(parent.start)
                        end.linkTo(startVerticalGuideline)
                    }
                ) { genres() }

                Box(
                    modifier = Modifier.constrainAs(overviewRef) {
                        width = Dimension.fillToConstraints
                        top.linkTo(ratingsRef.bottom, margin = spacing)
                        start.linkTo(startVerticalGuideline, margin = spacing)
                        end.linkTo(parent.end, margin = spacing)
                    }
                ) { overview() }

                Box(
                    modifier = Modifier.constrainAs(videosRef) {
                        width = Dimension.fillToConstraints
                        top.linkTo(overviewRef.bottom, margin = spacing)
                        start.linkTo(startVerticalGuideline)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = spacing)
                    }
                ) { videos() }
            }
            Box(modifier = Modifier.fillMaxSize().padding(horizontal = spacing)) {
                credits()
            }
        }
    }
}

@Composable
@Preview(device = Devices.DEFAULT)
@Preview(device = Devices.FOLDABLE)
@Preview(device = Devices.TABLET)
@Preview(widthDp = 900, heightDp = 1500)
private fun ScreenplayDetailsLayoutPreview() {
    CineScoutTheme {
        Adaptive { windowSizeClass ->
            ScreenplayDetailsLayout(
                mode = ScreenplayDetailsLayout.Mode.forClass(windowSizeClass),
                backdrops = {
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red),
                        text = "backdrops"
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
                ratings = {
                    Text(
                        modifier = Modifier
                            .height(32.dp)
                            .fillMaxWidth(0.5f)
                            .background(Color.Magenta),
                        text = "ratings"
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
                credits = {
                    Text(
                        modifier = Modifier
                            .height(72.dp)
                            .fillMaxWidth()
                            .background(Color.Green),
                        text = "actors"
                    )
                },
                overview = {
                    Text(
                        modifier = Modifier
                            .height(72.dp)
                            .fillMaxWidth()
                            .background(Color.White),
                        text = "Overview"
                    )
                },
                videos = {
                    Text(
                        modifier = Modifier
                            .height(72.dp)
                            .fillMaxWidth()
                            .background(Color.Gray),
                        text = "Videos"
                    )
                }
            )
        }
    }
}
