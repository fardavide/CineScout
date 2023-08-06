package cinescout.details.presentation.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.ConstraintSetScope
import androidx.constraintlayout.compose.Dimension
import cinescout.design.PlainAdaptivePreviews
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.details.presentation.model.DetailsSeasonsUiModel
import cinescout.details.presentation.model.ScreenplayDetailsUiModel
import cinescout.details.presentation.sample.ScreenplayDetailsUiModelSample
import cinescout.details.presentation.state.DetailsSeasonsState
import cinescout.details.presentation.ui.component.DetailsBackdrops
import cinescout.details.presentation.ui.component.DetailsCredits
import cinescout.details.presentation.ui.component.DetailsGenres
import cinescout.details.presentation.ui.component.DetailsInfoBox
import cinescout.details.presentation.ui.component.DetailsOverview
import cinescout.details.presentation.ui.component.DetailsPoster
import cinescout.details.presentation.ui.component.DetailsPublicRating
import cinescout.details.presentation.ui.component.DetailsSeasons
import cinescout.details.presentation.ui.component.DetailsTopBar
import cinescout.details.presentation.ui.component.DetailsVideos
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.presentation.ui.ScreenplayTypeBadge
import cinescout.utils.compose.Adaptive
import cinescout.utils.compose.thenIf

@Composable
internal fun ScreenplayDetailsBody(
    uiModel: ScreenplayDetailsUiModel,
    actions: ScreenplayDetailsBody.Actions,
    mode: ScreenplayDetailsScreen.Mode
) {
    val scrollState = rememberScrollState()
    when (mode) {
        is ScreenplayDetailsScreen.Mode.OnePane -> Layout(
            modifier = Modifier.verticalScroll(scrollState),
            uiModel = uiModel,
            actions = actions,
            mode = mode,
            constraintSet = onePaneConstraintSet(mode.spacing),
            shouldClipBackdrops = false,
            shouldShowTopBar = true,
            shouldShowCredits = true
        )
        ScreenplayDetailsScreen.Mode.TwoPane -> Layout(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .verticalScroll(scrollState),
            uiModel = uiModel,
            actions = actions,
            mode = mode,
            constraintSet = twoPaneConstraintSet(Dimens.Margin.medium, ratio = 0.5f),
            shouldClipBackdrops = true,
            shouldShowTopBar = true,
            shouldShowCredits = true
        )
        ScreenplayDetailsScreen.Mode.ThreePane -> Row(modifier = Modifier.fillMaxSize()) {
            val spacing = Dimens.Margin.medium
            Layout(
                modifier = Modifier
                    .fillMaxWidth(0.77f)
                    .verticalScroll(scrollState),
                uiModel = uiModel,
                actions = actions,
                mode = mode,
                constraintSet = twoPaneConstraintSet(spacing, ratio = 0.32f),
                shouldClipBackdrops = true,
                shouldShowTopBar = false,
                shouldShowCredits = false
            )
            DetailsCredits(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = spacing),
                mode = DetailsCredits.Mode.VerticalList,
                creditsMembers = uiModel.creditsMembers,
                openCredits = actions.openCredits
            )
        }
    }
}

@Composable
private fun Layout(
    uiModel: ScreenplayDetailsUiModel,
    actions: ScreenplayDetailsBody.Actions,
    mode: ScreenplayDetailsScreen.Mode,
    constraintSet: ConstraintSet,
    shouldClipBackdrops: Boolean,
    shouldShowTopBar: Boolean,
    shouldShowCredits: Boolean,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier.padding(bottom = Dimens.Margin.large),
        constraintSet = constraintSet
    ) {
        DetailsBackdrops(
            modifier = Modifier
                .layoutId(ScreenplayDetailsBody.Ids.Backdrops)
                .thenIf(shouldClipBackdrops) { clip(MaterialTheme.shapes.extraLarge) },
            urls = uiModel.backdrops
        )
        if (shouldShowCredits) {
            DetailsCredits(
                modifier = Modifier.layoutId(ScreenplayDetailsBody.Ids.Credits),
                mode = DetailsCredits.Mode.from(mode),
                creditsMembers = uiModel.creditsMembers,
                openCredits = actions.openCredits
            )
        }
        DetailsGenres(
            modifier = Modifier.layoutId(ScreenplayDetailsBody.Ids.Genres),
            genres = uiModel.genres
        )
        DetailsInfoBox(
            modifier = Modifier.layoutId(ScreenplayDetailsBody.Ids.InfoBox),
            title = uiModel.title,
            premiere = uiModel.premiere,
            runtime = uiModel.runtime
        )
        DetailsOverview(
            modifier = Modifier.layoutId(ScreenplayDetailsBody.Ids.Overview),
            tagline = uiModel.tagline,
            overview = uiModel.overview
        )
        DetailsPoster(
            modifier = Modifier.layoutId(ScreenplayDetailsBody.Ids.Poster),
            url = uiModel.posterUrl
        )
        DetailsPublicRating(
            modifier = Modifier.layoutId(ScreenplayDetailsBody.Ids.Ratings),
            average = uiModel.ratingAverage,
            count = uiModel.ratingCount
        )
        when (uiModel.seasonsState) {
            is DetailsSeasonsState.Data -> DetailsSeasons(
                modifier = Modifier.layoutId(ScreenplayDetailsBody.Ids.Seasons),
                uiModel = uiModel.seasonsState.uiModel,
                openSeasons = actions.openSeasons
            )
            is DetailsSeasonsState.Error, DetailsSeasonsState.Loading, DetailsSeasonsState.NoSeasons -> Unit
        }
        if (shouldShowTopBar) {
            DetailsTopBar(
                modifier = Modifier.layoutId(ScreenplayDetailsBody.Ids.TopBar),
                back = actions.back
            )
        }
        ScreenplayTypeBadge(
            modifier = Modifier.layoutId(ScreenplayDetailsBody.Ids.TypeBadge),
            type = ScreenplayType.from(uiModel.ids)
        )
        DetailsVideos(
            modifier = Modifier.layoutId(ScreenplayDetailsBody.Ids.Videos),
            videos = uiModel.videos
        )
    }
}

@Composable
private fun onePaneConstraintSet(spacing: Dp) = ConstraintSet {
    with(createRefs()) {
        constrain(topBar) {
            width = Dimension.wrapContent
            height = Dimension.wrapContent
            top.linkTo(parent.top, margin = Dimens.Margin.small)
            start.linkTo(parent.start, margin = Dimens.Margin.small)
        }

        constrain(backdrops) {
            width = Dimension.fillToConstraints
            height = Dimension.ratio("3:2")
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(typeBadge) {
            width = Dimension.wrapContent
            height = Dimension.wrapContent
            bottom.linkTo(backdrops.bottom, margin = spacing)
            end.linkTo(parent.end, margin = spacing)
        }

        constrain(poster) {
            width = Dimension.percent(0.3f)
            height = Dimension.ratio("1:1.5")
            top.linkTo(backdrops.bottom)
            bottom.linkTo(backdrops.bottom)
            start.linkTo(parent.start, margin = spacing)
        }

        constrain(infoBox) {
            width = Dimension.fillToConstraints
            top.linkTo(backdrops.bottom, margin = spacing)
            start.linkTo(poster.end, margin = spacing)
            end.linkTo(parent.end, margin = spacing)
        }

        val barrier = createBottomBarrier(poster, infoBox)

        constrain(ratings) {
            width = Dimension.preferredWrapContent
            top.linkTo(barrier, margin = spacing)
            start.linkTo(parent.start, margin = spacing)
            end.linkTo(parent.end, margin = spacing)
        }

        constrain(genres) {
            width = Dimension.preferredWrapContent
            top.linkTo(ratings.bottom, margin = spacing)
            start.linkTo(parent.start)
            bottom.linkTo(seasons.top)
            end.linkTo(parent.end)
        }

        constrain(seasons) {
            width = Dimension.fillToConstraints
            top.linkTo(genres.bottom, margin = spacing)
            bottom.linkTo(credits.top, margin = spacing)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(credits) {
            width = Dimension.fillToConstraints
            top.linkTo(seasons.bottom, margin = spacing)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(overview) {
            width = Dimension.fillToConstraints
            top.linkTo(credits.bottom, margin = spacing)
            start.linkTo(parent.start, margin = spacing)
            end.linkTo(parent.end, margin = spacing)
        }

        constrain(videos) {
            width = Dimension.fillToConstraints
            top.linkTo(overview.bottom, margin = spacing)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom, margin = spacing)
        }
    }
}

@Composable
private fun twoPaneConstraintSet(spacing: Dp, ratio: Float) = ConstraintSet {
    val startVerticalGuideline = createGuidelineFromStart(ratio)
    with(createRefs()) {
        constrain(topBar) {
            width = Dimension.wrapContent
            height = Dimension.wrapContent
            top.linkTo(parent.top, margin = Dimens.Margin.small)
            start.linkTo(parent.start, margin = Dimens.Margin.small)
        }

        constrain(backdrops) {
            width = Dimension.fillToConstraints
            height = Dimension.ratio("3:2")
            top.linkTo(parent.top)
            start.linkTo(startVerticalGuideline)
            end.linkTo(parent.end)
        }

        constrain(typeBadge) {
            width = Dimension.wrapContent
            height = Dimension.wrapContent
            bottom.linkTo(backdrops.bottom, margin = spacing)
            end.linkTo(parent.end, margin = spacing)
        }

        constrain(poster) {
            width = Dimension.fillToConstraints
            height = Dimension.ratio("1:1.5")
            top.linkTo(parent.top)
            start.linkTo(parent.start, margin = spacing)
            end.linkTo(startVerticalGuideline, margin = spacing)
        }

        constrain(infoBox) {
            width = Dimension.fillToConstraints
            top.linkTo(poster.bottom, margin = spacing)
            start.linkTo(parent.start, margin = spacing)
            end.linkTo(startVerticalGuideline, margin = spacing)
        }

        constrain(ratings) {
            width = Dimension.preferredWrapContent
            top.linkTo(backdrops.bottom, margin = spacing)
            start.linkTo(startVerticalGuideline, margin = spacing)
            end.linkTo(parent.end, margin = spacing)
        }

        constrain(genres) {
            width = Dimension.preferredWrapContent
            top.linkTo(infoBox.bottom, margin = spacing)
            start.linkTo(parent.start)
            end.linkTo(startVerticalGuideline)
        }

        constrain(seasons) {
            width = Dimension.fillToConstraints
            top.linkTo(genres.bottom, margin = spacing)
            start.linkTo(parent.start)
            end.linkTo(startVerticalGuideline)
        }

        constrain(credits) {
            width = Dimension.fillToConstraints
            top.linkTo(seasons.bottom, margin = spacing)
            start.linkTo(parent.start)
            end.linkTo(startVerticalGuideline)
        }

        constrain(overview) {
            width = Dimension.fillToConstraints
            top.linkTo(ratings.bottom, margin = spacing)
            start.linkTo(startVerticalGuideline, margin = spacing)
            end.linkTo(parent.end, margin = spacing)
        }

        constrain(videos) {
            width = Dimension.fillToConstraints
            top.linkTo(overview.bottom, margin = spacing)
            start.linkTo(startVerticalGuideline)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom, margin = spacing)
        }
    }
}

private fun ConstraintSetScope.createRefs(): ScreenplayDetailsBody.Refs {
    val (
        backdrops,
        credits,
        genres,
        infoBox,
        overview,
        poster,
        ratings,
        seasons,
        topBar,
        typeBadge,
        videos
    ) = createRefsFor(
        ScreenplayDetailsBody.Ids.Backdrops,
        ScreenplayDetailsBody.Ids.Credits,
        ScreenplayDetailsBody.Ids.Genres,
        ScreenplayDetailsBody.Ids.InfoBox,
        ScreenplayDetailsBody.Ids.Overview,
        ScreenplayDetailsBody.Ids.Poster,
        ScreenplayDetailsBody.Ids.Ratings,
        ScreenplayDetailsBody.Ids.Seasons,
        ScreenplayDetailsBody.Ids.TopBar,
        ScreenplayDetailsBody.Ids.TypeBadge,
        ScreenplayDetailsBody.Ids.Videos
    )

    return ScreenplayDetailsBody.Refs(
        backdrops = backdrops,
        credits = credits,
        genres = genres,
        infoBox = infoBox,
        overview = overview,
        poster = poster,
        ratings = ratings,
        seasons = seasons,
        topBar = topBar,
        typeBadge = typeBadge,
        videos = videos
    )
}

object ScreenplayDetailsBody {

    data class Actions(
        val back: () -> Unit,
        val openCredits: () -> Unit,
        val openSeasons: (DetailsSeasonsUiModel) -> Unit
    ) {
        companion object {
            val Empty = Actions(back = {}, openCredits = {}, openSeasons = {})
        }
    }

    data class Refs(
        val backdrops: ConstrainedLayoutReference,
        val credits: ConstrainedLayoutReference,
        val genres: ConstrainedLayoutReference,
        val infoBox: ConstrainedLayoutReference,
        val overview: ConstrainedLayoutReference,
        val poster: ConstrainedLayoutReference,
        val ratings: ConstrainedLayoutReference,
        val seasons: ConstrainedLayoutReference,
        val topBar: ConstrainedLayoutReference,
        val typeBadge: ConstrainedLayoutReference,
        val videos: ConstrainedLayoutReference
    )

    object Ids {

        const val Backdrops = "backdrops"
        const val Credits = "credits"
        const val Genres = "genres"
        const val InfoBox = "infoBox"
        const val Overview = "overview"
        const val Poster = "poster"
        const val Ratings = "ratings"
        const val Seasons = "seasons"
        const val TopBar = "topBar"
        const val TypeBadge = "typeBadge"
        const val Videos = "videos"
    }
}

@Composable
@PlainAdaptivePreviews
private fun ScreenplayDetailsBodyPreview() {
    val uiModel = ScreenplayDetailsUiModelSample.Inception
    CineScoutTheme {
        Adaptive { windowSizeClass ->
            ScreenplayDetailsBody(
                uiModel = uiModel,
                actions = ScreenplayDetailsBody.Actions.Empty,
                mode = ScreenplayDetailsScreen.Mode.forClass(windowSizeClass)
            )
        }
    }
}
