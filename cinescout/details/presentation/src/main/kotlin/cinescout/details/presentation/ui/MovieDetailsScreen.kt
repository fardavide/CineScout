package cinescout.details.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.ErrorScreen
import cinescout.design.util.NoContentDescription
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.details.presentation.model.MovieDetailsAction
import cinescout.details.presentation.model.MovieDetailsState
import cinescout.details.presentation.model.MovieDetailsUiModel
import cinescout.details.presentation.previewdata.MovieDetailsScreenPreviewDataProvider
import cinescout.details.presentation.viewmodel.MovieDetailsViewModel
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import studio.forface.cinescout.design.R.drawable
import studio.forface.cinescout.design.R.string

@Composable
fun MovieDetailsScreen(movieId: TmdbMovieId, actions: MovieDetailsScreen.Actions, modifier: Modifier = Modifier) {
    val viewModel: MovieDetailsViewModel = koinViewModel(parameters = { parametersOf(movieId) })
    val state by viewModel.state.collectAsStateLifecycleAware()
    MovieDetailsScreen(
        state = state,
        actions = actions,
        rateMovie = { viewModel.submit(MovieDetailsAction.RateMovie(it)) },
        modifier = modifier
    )
}

@Composable
fun MovieDetailsScreen(
    state: MovieDetailsState,
    actions: MovieDetailsScreen.Actions,
    rateMovie: (Rating) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .testTag(TestTag.MovieDetails)
            .navigationBarsPadding(),
        bottomBar = { MovieDetailsBottomBar(actions.onBack) }
    ) { paddingValues ->
        val layoutDirection = LocalLayoutDirection.current
        Surface(
            modifier = Modifier.padding(
                start = paddingValues.calculateStartPadding(layoutDirection),
                end = paddingValues.calculateEndPadding(layoutDirection),
                bottom = paddingValues.calculateBottomPadding()
            )
        ) {
            MovieDetailsContent(state = state, rateMovie = rateMovie)
        }
    }
}

@Composable
fun MovieDetailsContent(state: MovieDetailsState, rateMovie: (Rating) -> Unit) {
    var shouldShowRateDialog by remember { mutableStateOf(false) }
    when (state) {
        is MovieDetailsState.Data -> {
            if (shouldShowRateDialog) {
                val actions = RateMovieDialog.Actions(
                    onDismissRequest = { shouldShowRateDialog = false },
                    saveRating = { rateMovie(it) }
                )
                RateMovieDialog(
                    movieTitle = state.movieDetails.title,
                    moviePersonalRating = state.movieDetails.ratings.personal.rating,
                    actions = actions
                )
            }
            MovieDetailsLayout(
                backdrop = { Backdrop(url = state.movieDetails.backdropUrl) },
                poster = { Poster(url = state.movieDetails.posterUrl) },
                infoBox = { InfoBox(title = state.movieDetails.title, releaseDate = state.movieDetails.releaseDate) },
                ratings = {
                    Ratings(
                        ratings = state.movieDetails.ratings,
                        openRateDialog = { shouldShowRateDialog = true }
                    )
                },
                genres = { /*TODO*/ },
                actors = { /*TODO*/ }
            )
        }
        is MovieDetailsState.Error -> ErrorScreen(text = state.message)
        MovieDetailsState.Loading -> CenteredProgress()
    }
}

@Composable
private fun Backdrop(url: String?) {
    AsyncImage(
        modifier = Modifier
            .fillMaxSize()
            .imageBackground(),
        model = url,
        contentDescription = NoContentDescription,
        contentScale = ContentScale.Crop,
        error = painterResource(id = drawable.ic_warning_30)
    )
}

@Composable
private fun Poster(url: String?) {
    AsyncImage(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.medium)
            .imageBackground(),
        model = url,
        contentDescription = NoContentDescription,
        error = painterResource(id = drawable.ic_warning_30)
    )
}

@Composable
private fun InfoBox(title: String, releaseDate: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(Dimens.Margin.Small)
    ) {
        Text(text = title, maxLines = 2, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(Dimens.Margin.Small))
        Text(text = releaseDate, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun Ratings(ratings: MovieDetailsUiModel.Ratings, openRateDialog: () -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = Dimens.Margin.Medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .border(
                    width = Dimens.Outline,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = MaterialTheme.shapes.small
                )
                .padding(Dimens.Margin.Medium)
                .width(Dimens.Icon.Medium)
                .height(Dimens.Icon.Small),
            model = drawable.img_tmdb_logo_short,
            contentDescription = stringResource(id = string.tmdb_logo_description),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.width(Dimens.Margin.Small))
        Column {
            Text(text = ratings.publicAverage, style = MaterialTheme.typography.titleMedium)
            Text(text = ratings.publicCount, style = MaterialTheme.typography.bodySmall)
        }
    }
    PersonalRating(rating = ratings.personal, openRateDialog = openRateDialog)
}

@Composable
private fun PersonalRating(rating: MovieDetailsUiModel.Ratings.Personal, openRateDialog: () -> Unit) {
    FilledTonalButton(
        onClick = openRateDialog,
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(horizontal = Dimens.Margin.Medium, vertical = Dimens.Margin.Small)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            when (rating) {
                MovieDetailsUiModel.Ratings.Personal.NotRated -> {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = NoContentDescription)
                    Text(text = stringResource(id = string.details_rate_now))
                }
                is MovieDetailsUiModel.Ratings.Personal.Rated -> Text(
                    text = rating.stringValue,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
private fun MovieDetailsLayout(
    backdrop: @Composable () -> Unit,
    poster: @Composable () -> Unit,
    infoBox: @Composable () -> Unit,
    ratings: @Composable RowScope.() -> Unit,
    genres: @Composable () -> Unit,
    actors: @Composable () -> Unit
) {
    val spacing = Dimens.Margin.Medium
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (backdropRef, posterRef, infoBoxRef, ratingsRef, genresRef, actorsRef) = createRefs()

        Box(
            modifier = Modifier.constrainAs(backdropRef) {
                width = Dimension.fillToConstraints
                height = Dimension.ratio("3:2")
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) { backdrop() }

        Box(
            modifier = Modifier.constrainAs(posterRef) {
                width = Dimension.percent(0.3f)
                height = Dimension.ratio("1:1.5")
                top.linkTo(backdropRef.bottom)
                bottom.linkTo(backdropRef.bottom)
                start.linkTo(parent.start, margin = spacing)
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
    }
}

@Composable
private fun MovieDetailsBottomBar(onBack: () -> Unit) {
    BottomAppBar(actions = {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = stringResource(id = string.back_button_description)
            )
        }
    })
}

object MovieDetailsScreen {

    const val MovieIdKey = "movie_id"

    data class Actions(
        val onBack: () -> Unit
    ) {

        companion object {

            val Empty = Actions(onBack = {})
        }
    }
}

@Composable
@Preview
@Preview(device = Devices.TABLET)
private fun MovieDetailsScreenPreview(
    @PreviewParameter(MovieDetailsScreenPreviewDataProvider::class) state: MovieDetailsState
) {
    CineScoutTheme {
        MovieDetailsScreen(state = state, actions = MovieDetailsScreen.Actions.Empty, rateMovie = {})
    }
}
