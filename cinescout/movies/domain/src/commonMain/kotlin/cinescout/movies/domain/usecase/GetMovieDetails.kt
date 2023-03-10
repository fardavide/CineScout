package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.store5.StoreFlow
import org.koin.core.annotation.Factory

@Factory
class GetMovieDetails(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(movieId: TmdbMovieId, refresh: Boolean = false): StoreFlow<MovieWithDetails> =
        movieRepository.getMovieDetails(movieId, refresh)
}
