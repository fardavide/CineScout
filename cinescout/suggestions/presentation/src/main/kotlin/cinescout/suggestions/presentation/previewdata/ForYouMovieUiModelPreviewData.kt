package cinescout.suggestions.presentation.previewdata

import cinescout.movies.domain.model.TmdbBackdropImage
import cinescout.movies.domain.model.TmdbPosterImage
import cinescout.movies.domain.model.TmdbProfileImage
import cinescout.movies.domain.testdata.MovieCreditsTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.suggestions.presentation.model.ForYouMovieUiModel

object ForYouMovieUiModelPreviewData {

    val Inception = ForYouMovieUiModel(
        tmdbMovieId = MovieTestData.Inception.tmdbId,
        actors = MovieCreditsTestData.Inception.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouMovieUiModel.Actor(imageUrl.orNull().orEmpty())
        },
        backdropUrl = MovieTestData.Inception.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        posterUrl = MovieTestData.Inception.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = MovieTestData.Inception.rating.average.value.toString(),
        releaseYear = MovieTestData.Inception.releaseDate.year.toString(),
        title = MovieTestData.Inception.title
    )

    val TheWolfOfWallStreet = ForYouMovieUiModel(
        tmdbMovieId = MovieTestData.TheWolfOfWallStreet.tmdbId,
        actors = MovieCreditsTestData.TheWolfOfWallStreet.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouMovieUiModel.Actor(imageUrl.orNull().orEmpty())
        },
        backdropUrl = MovieTestData.TheWolfOfWallStreet.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        posterUrl = MovieTestData.TheWolfOfWallStreet.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = MovieTestData.TheWolfOfWallStreet.rating.average.value.toString(),
        releaseYear = MovieTestData.TheWolfOfWallStreet.releaseDate.year.toString(),
        title = MovieTestData.TheWolfOfWallStreet.title
    )
}
