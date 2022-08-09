package cinescout.suggestions.domain.usecase

import arrow.core.NonEmptyList
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.MovieWithExtras
import cinescout.movies.domain.model.ReleaseYear

class BuildDiscoverMoviesParams {

    operator fun invoke(positiveMovies: NonEmptyList<MovieWithExtras>) = DiscoverMoviesParams(
        genre = positiveMovies.random().movieWithDetails.genres.random(),
        releaseYear = ReleaseYear(positiveMovies.random().movieWithDetails.movie.releaseDate.year)
    )
}
