package cinescout.details.presentation.previewdata

import arrow.core.Option
import cinescout.details.presentation.model.MovieDetailsUiModel
import cinescout.movies.domain.model.TmdbBackdropImage
import cinescout.movies.domain.model.TmdbPosterImage
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import com.soywiz.klock.Date

object MovieDetailsUiModelPreviewData {

    val Inception = MovieDetailsUiModel(
        backdropUrl = MovieTestData.Inception.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        posterUrl = MovieTestData.Inception.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        ratings = MovieDetailsUiModel.Ratings(
            publicAverage = MovieTestData.Inception.rating.average.value.toString(),
            publicCount = MovieTestData.Inception.rating.voteCount.toString(),
            personal = MovieWithExtrasTestData.Inception.personalRating.fold(
                ifEmpty = { MovieDetailsUiModel.Ratings.Personal.NotRated },
                ifSome = { MovieDetailsUiModel.Ratings.Personal.Rated(it.value.toInt().toString()) }
            )
        ),
        releaseDate = MovieTestData.Inception.releaseDate.format(),
        title = MovieTestData.Inception.title,
        tmdbId = MovieTestData.Inception.tmdbId
    )

    val TheWolfOfWallStreet = MovieDetailsUiModel(
        backdropUrl = MovieTestData.TheWolfOfWallStreet.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        posterUrl = MovieTestData.TheWolfOfWallStreet.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.LARGE),
        ratings = MovieDetailsUiModel.Ratings(
            publicAverage = MovieTestData.TheWolfOfWallStreet.rating.average.value.toString(),
            publicCount = MovieTestData.TheWolfOfWallStreet.rating.voteCount.toString(),
            personal = MovieWithExtrasTestData.TheWolfOfWallStreet.personalRating.fold(
                ifEmpty = { MovieDetailsUiModel.Ratings.Personal.NotRated },
                ifSome = { MovieDetailsUiModel.Ratings.Personal.Rated(it.value.toInt().toString()) }
            )
        ),
        releaseDate = MovieTestData.TheWolfOfWallStreet.releaseDate.format(),
        title = MovieTestData.TheWolfOfWallStreet.title,
        tmdbId = MovieTestData.TheWolfOfWallStreet.tmdbId
    )

    private fun Option<Date>.format() = fold(ifEmpty = { "" }, ifSome = { it.format("MMM YYYY") })
}
