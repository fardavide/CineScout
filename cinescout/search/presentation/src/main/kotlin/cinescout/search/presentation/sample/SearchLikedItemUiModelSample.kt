package cinescout.search.presentation.sample

import cinescout.common.model.TmdbPosterImage
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.search.presentation.model.SearchLikedItemUiModel
import cinescout.tvshows.domain.testdata.TvShowTestData

object SearchLikedItemUiModelSample {

    val Dexter = SearchLikedItemUiModel(
        tvShowId = TvShowTestData.Dexter.tmdbId,
        posterUrl = TvShowTestData.Dexter.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.SMALL),
        title = TvShowTestData.Dexter.title
    )

    val Grimm = SearchLikedItemUiModel(
        tvShowId = TvShowTestData.Grimm.tmdbId,
        posterUrl = TvShowTestData.Grimm.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.SMALL),
        title = TvShowTestData.Grimm.title
    )

    val Inception = SearchLikedItemUiModel(
        movieId = MovieTestData.Inception.tmdbId,
        posterUrl = MovieTestData.Inception.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.SMALL),
        title = MovieTestData.Inception.title
    )

    val TheWolfOfWallStreet = SearchLikedItemUiModel(
        movieId = MovieTestData.TheWolfOfWallStreet.tmdbId,
        posterUrl = MovieTestData.TheWolfOfWallStreet.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.SMALL),
        title = MovieTestData.TheWolfOfWallStreet.title
    )
}
