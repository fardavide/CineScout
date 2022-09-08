package cinescout.details.presentation.mapper

import cinescout.details.presentation.model.MovieDetailsUiModel
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieWithExtras
import cinescout.movies.domain.model.TmdbBackdropImage
import cinescout.movies.domain.model.TmdbPosterImage
import cinescout.movies.domain.model.TmdbProfileImage

class MovieDetailsUiModelMapper {

    fun toUiModel(movieWithExtras: MovieWithExtras): MovieDetailsUiModel {
        val movie = movieWithExtras.movieWithDetails.movie
        return MovieDetailsUiModel(
            backdropUrl = movie.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
            creditsMember = movieWithExtras.credits.members(),
            genres = movieWithExtras.movieWithDetails.genres.map { it.name },
            isInWatchlist = movieWithExtras.isInWatchlist,
            posterUrl = movie.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.LARGE),
            ratings = MovieDetailsUiModel.Ratings(
                publicAverage = movie.rating.average.value.toString(),
                publicCount = movie.rating.voteCount.toString(),
                personal = movieWithExtras.personalRating.fold(
                    ifEmpty = { MovieDetailsUiModel.Ratings.Personal.NotRated },
                    ifSome = { rating ->
                        MovieDetailsUiModel.Ratings.Personal.Rated(
                            rating = rating,
                            stringValue = rating.value.toInt().toString()
                        )
                    }
                )
            ),
            releaseDate = movie.releaseDate.fold(ifEmpty = { "" }, ifSome = { it.format("MMM YYYY") }),
            title = movie.title,
            tmdbId = movie.tmdbId
        )
    }

    private fun MovieCredits.members(): List<MovieDetailsUiModel.CreditsMember> =
        (cast + crew).map { member ->
            MovieDetailsUiModel.CreditsMember(
                name = member.person.name,
                profileImageUrl = member.person.profileImage.orNull()?.getUrl(TmdbProfileImage.Size.SMALL),
                role = when (member) {
                    is MovieCredits.CastMember -> member.character.orNull()
                    is MovieCredits.CrewMember -> member.job.orNull()
                }
            )
        }
}
