package cinescout.lists.presentation.sample

import arrow.core.Nel
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import cinescout.tvshows.domain.sample.TvShowSample
import cinescout.tvshows.domain.sample.TvShowWithPersonalRatingSample

object ListItemUiModelSample {

    val BreakingBad = ListItemUiModel.TvShow(
        personalRating = TvShowWithPersonalRatingSample.BreakingBad.personalRatingString(),
        posterUrl = TvShowSample.BreakingBad.posterUrl(),
        rating = TvShowSample.BreakingBad.ratingString(),
        title = TvShowSample.BreakingBad.title,
        tmdbId = TvShowSample.BreakingBad.tmdbId
    )

    val Dexter = ListItemUiModel.TvShow(
        personalRating = TvShowWithPersonalRatingSample.Dexter.personalRatingString(),
        posterUrl = TvShowSample.Dexter.posterUrl(),
        rating = TvShowSample.Dexter.ratingString(),
        title = TvShowSample.Dexter.title,
        tmdbId = TvShowSample.Dexter.tmdbId
    )

    val Inception = ListItemUiModel.Movie(
        personalRating = MovieWithPersonalRatingSample.Inception.personalRatingString(),
        posterUrl = ScreenplaySample.Inception.posterUrl(),
        rating = ScreenplaySample.Inception.ratingString(),
        title = ScreenplaySample.Inception.title,
        tmdbId = ScreenplaySample.Inception.tmdbId
    )

    val TheWolfOfWallStreet = ListItemUiModel.Movie(
        personalRating = MovieWithPersonalRatingSample.TheWolfOfWallStreet.personalRatingString(),
        posterUrl = ScreenplaySample.TheWolfOfWallStreet.posterUrl(),
        rating = ScreenplaySample.TheWolfOfWallStreet.ratingString(),
        title = ScreenplaySample.TheWolfOfWallStreet.title,
        tmdbId = ScreenplaySample.TheWolfOfWallStreet.tmdbId
    )

    private fun ListItemUiModel.withoutPersonalRating() = when (this) {
        is ListItemUiModel.Movie -> copy(personalRating = null)
        is ListItemUiModel.TvShow -> copy(personalRating = null)
    }

    fun Nel<ListItemUiModel>.withoutPersonalRating() = map { it.withoutPersonalRating() }

    private fun MovieWithPersonalRating.personalRatingString() = personalRating.value.toString()
    private fun Movie.posterUrl() = posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM)
    private fun Movie.ratingString() = rating.average.value.toString()

    private fun TvShowWithPersonalRating.personalRatingString() = personalRating.value.toString()
    private fun TvShow.posterUrl() = posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM)
    private fun TvShow.ratingString() = rating.average.value.toString()
}
