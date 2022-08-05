package cinescout.suggestions.presentation.mapper

import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.TmdbProfileImage
import cinescout.suggestions.presentation.model.ForYouMovieUiModel

class ForYouMovieUiModelMapper {

    fun toUiModel(movie: Movie, credits: MovieCredits) = ForYouMovieUiModel(
        actors = toActorsUiModels(credits.cast),
        backdropUrl = null, // TODO: movie.backdrop
        posterUrl = "", // TODO: movie.poster
        rating = "idk", // TODO: movie.rating.format
        releaseYear = movie.releaseDate.year.toString(),
        title = movie.title,
        tmdbMovieId = movie.tmdbId
    )

    private fun toActorsUiModels(actors: List<MovieCredits.CastMember>): List<ForYouMovieUiModel.Actor> =
        actors.map { member ->
            val profileImageUrl = member.person.profileImage
                .map { image -> image.getUrl(TmdbProfileImage.Size.SMALL) }
                .orNull()
            ForYouMovieUiModel.Actor(profileImageUrl = profileImageUrl)
        }
}
