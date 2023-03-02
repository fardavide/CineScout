package cinescout.search.presentation.sample

import cinescout.movies.domain.sample.MovieSample
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.search.presentation.model.SearchLikedItemUiModel
import cinescout.tvshows.domain.sample.TvShowSample

object SearchLikedItemUiModelSample {

    val Dexter = SearchLikedItemUiModel(
        tvShowId = TvShowSample.Dexter.tmdbId,
        posterUrl = TvShowSample.Dexter.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.SMALL),
        title = TvShowSample.Dexter.title
    )

    val Grimm = SearchLikedItemUiModel(
        tvShowId = TvShowSample.Grimm.tmdbId,
        posterUrl = TvShowSample.Grimm.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.SMALL),
        title = TvShowSample.Grimm.title
    )

    val Inception = SearchLikedItemUiModel(
        movieId = MovieSample.Inception.tmdbId,
        posterUrl = MovieSample.Inception.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.SMALL),
        title = MovieSample.Inception.title
    )

    val TheWolfOfWallStreet = SearchLikedItemUiModel(
        movieId = MovieSample.TheWolfOfWallStreet.tmdbId,
        posterUrl = MovieSample.TheWolfOfWallStreet.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.SMALL),
        title = MovieSample.TheWolfOfWallStreet.title
    )
}
