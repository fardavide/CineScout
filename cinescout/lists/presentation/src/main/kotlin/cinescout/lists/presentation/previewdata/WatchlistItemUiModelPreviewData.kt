package cinescout.lists.presentation.previewdata

import cinescout.lists.presentation.model.WatchlistItemUiModel
import cinescout.movies.domain.model.TmdbPosterImage
import cinescout.movies.domain.testdata.MovieTestData

object WatchlistItemUiModelPreviewData {

    val Inception = WatchlistItemUiModel(
        posterUrl = MovieTestData.Inception.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        title = MovieTestData.Inception.title,
        tmdbId = MovieTestData.Inception.tmdbId
    )

    val TheWolfOfWallStreet = WatchlistItemUiModel(
        posterUrl = MovieTestData.TheWolfOfWallStreet.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        title = MovieTestData.TheWolfOfWallStreet.title,
        tmdbId = MovieTestData.TheWolfOfWallStreet.tmdbId
    )
}
