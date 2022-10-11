package cinescout.suggestions.presentation.mapper

import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbPosterImage
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieWithExtras
import cinescout.movies.domain.model.TmdbProfileImage
import cinescout.suggestions.presentation.model.ForYouMovieUiModel

class ForYouMovieUiModelMapper {

    fun toUiModel(movieWithExtras: MovieWithExtras): ForYouMovieUiModel {
        val credits = movieWithExtras.credits
        val movie = movieWithExtras.movieWithDetails.movie
        return ForYouMovieUiModel(
            actors = toActorsUiModels(credits.cast),
            backdropUrl = movie.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
            genres = movieWithExtras.movieWithDetails.genres.map { genre -> genre.name },
            posterUrl = movie.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
            rating = movie.rating.average.value.toString(),
            releaseYear = movie.releaseDate.orNull()?.year?.toString().orEmpty(),
            title = movie.title,
            tmdbMovieId = movie.tmdbId
        )
    }

    private fun toActorsUiModels(actors: List<MovieCredits.CastMember>): List<ForYouMovieUiModel.Actor> =
        actors.map { member ->
            val profileImageUrl = member.person.profileImage
                .map { image -> image.getUrl(TmdbProfileImage.Size.SMALL) }
                .orNull()
            ForYouMovieUiModel.Actor(profileImageUrl = profileImageUrl)
        }
}
