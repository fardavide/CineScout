package cinescout.details.presentation.previewdata

import cinescout.details.presentation.model.MovieDetailsUiModel
import cinescout.movies.domain.testdata.MovieTestData

object MovieDetailsUiModelPreviewData {

    val Inception = MovieDetailsUiModel(
        title = MovieTestData.Inception.title,
        tmdbId = MovieTestData.Inception.tmdbId
    )

    val TheWolfOfWallStreet = MovieDetailsUiModel(
        title = MovieTestData.TheWolfOfWallStreet.title,
        tmdbId = MovieTestData.TheWolfOfWallStreet.tmdbId
    )
}
