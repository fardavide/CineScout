package cinescout.suggestions.presentation.sample

import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbPosterImage
import cinescout.common.model.TmdbProfileImage
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.testdata.MovieCreditsTestData
import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import cinescout.suggestions.presentation.model.ForYouMovieUiModel

object ForYouMovieUiModelSample {

    val Inception = ForYouMovieUiModel(
        tmdbMovieId = MovieSample.Inception.tmdbId,
        actors = MovieCreditsTestData.Inception.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouMovieUiModel.Actor(imageUrl.orNull().orEmpty())
        },
        backdropUrl = MovieSample.Inception.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        genres = MovieWithExtrasTestData.Inception.movieWithDetails.genres.map { genre -> genre.name },
        posterUrl = MovieSample.Inception.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = MovieSample.Inception.rating.average.value.toString(),
        releaseYear = MovieSample.Inception.releaseDate.orNull()?.year.toString(),
        title = MovieSample.Inception.title
    )

    val TheWolfOfWallStreet = ForYouMovieUiModel(
        tmdbMovieId = MovieSample.TheWolfOfWallStreet.tmdbId,
        actors = MovieCreditsTestData.TheWolfOfWallStreet.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouMovieUiModel.Actor(imageUrl.orNull().orEmpty())
        },
        backdropUrl = MovieSample.TheWolfOfWallStreet.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        genres = MovieWithExtrasTestData.TheWolfOfWallStreet.movieWithDetails.genres.map { genre -> genre.name },
        posterUrl = MovieSample.TheWolfOfWallStreet.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = MovieSample.TheWolfOfWallStreet.rating.average.value.toString(),
        releaseYear = MovieSample.TheWolfOfWallStreet.releaseDate.orNull()?.year.toString(),
        title = MovieSample.TheWolfOfWallStreet.title
    )

    val War = ForYouMovieUiModel(
        tmdbMovieId = MovieSample.War.tmdbId,
        actors = MovieCreditsTestData.War.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouMovieUiModel.Actor(imageUrl.orNull().orEmpty())
        },
        backdropUrl = MovieSample.War.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
        genres = MovieWithExtrasTestData.War.movieWithDetails.genres.map { genre -> genre.name },
        posterUrl = MovieSample.War.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
        rating = MovieSample.War.rating.average.value.toString(),
        releaseYear = MovieSample.War.releaseDate.orNull()?.year.toString(),
        title = MovieSample.War.title
    )
}
