package cinescout.suggestions.presentation.previewdata

import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbPosterImage
import cinescout.movies.domain.model.TmdbProfileImage
import cinescout.movies.domain.testdata.MovieCreditsTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithExtrasTestData
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
        genres = MovieWithExtrasTestData.Inception.movieWithDetails.genres.map { genre -> genre.name },
        posterUrl = MovieTestData.Inception.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = MovieTestData.Inception.rating.average.value.toString(),
        releaseYear = MovieTestData.Inception.releaseDate.orNull()?.year.toString(),
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
        genres = MovieWithExtrasTestData.TheWolfOfWallStreet.movieWithDetails.genres.map { genre -> genre.name },
        posterUrl = MovieTestData.TheWolfOfWallStreet.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = MovieTestData.TheWolfOfWallStreet.rating.average.value.toString(),
        releaseYear = MovieTestData.TheWolfOfWallStreet.releaseDate.orNull()?.year.toString(),
        title = MovieTestData.TheWolfOfWallStreet.title
    )

    val War = ForYouMovieUiModel(
        tmdbMovieId = MovieTestData.War.tmdbId,
        actors = MovieCreditsTestData.War.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouMovieUiModel.Actor(imageUrl.orNull().orEmpty())
        },
        backdropUrl = MovieTestData.War.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        genres = MovieWithExtrasTestData.War.movieWithDetails.genres.map { genre -> genre.name },
        posterUrl = MovieTestData.War.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = MovieTestData.War.rating.average.value.toString(),
        releaseYear = MovieTestData.War.releaseDate.orNull()?.year.toString(),
        title = MovieTestData.War.title
    )
}
