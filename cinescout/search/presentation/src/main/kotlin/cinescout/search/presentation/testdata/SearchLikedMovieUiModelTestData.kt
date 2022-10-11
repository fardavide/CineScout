package cinescout.search.presentation.testdata

import cinescout.common.model.TmdbPosterImage
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.search.presentation.model.SearchLikedMovieUiModel

object SearchLikedMovieUiModelTestData {

    val Inception = SearchLikedMovieUiModel(
        movieId = MovieTestData.Inception.tmdbId,
        posterUrl = MovieTestData.Inception.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.SMALL),
        title = MovieTestData.Inception.title
    )

    val TheWolfOfWallStreet = SearchLikedMovieUiModel(
        movieId = MovieTestData.TheWolfOfWallStreet.tmdbId,
        posterUrl = MovieTestData.TheWolfOfWallStreet.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.SMALL),
        title = MovieTestData.TheWolfOfWallStreet.title
    )
}
