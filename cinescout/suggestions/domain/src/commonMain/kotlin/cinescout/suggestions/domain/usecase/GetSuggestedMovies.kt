package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.flatMap
import arrow.core.left
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.SuggestionError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetSuggestedMovies(
    private val buildDiscoverMoviesParams: BuildDiscoverMoviesParams,
    private val movieRepository: MovieRepository
) {

    operator fun invoke(): Flow<Either<SuggestionError, NonEmptyList<Movie>>> =
        buildDiscoverMoviesParams().flatMapLatest { paramsEither ->
            paramsEither.fold(
                ifLeft = { suggestionError -> flowOf(suggestionError.left()) },
                ifRight = { params -> discoverMovies(params) })
        }

    private fun discoverMovies(params: DiscoverMoviesParams): Flow<Either<SuggestionError, NonEmptyList<Movie>>> =
        movieRepository.discoverMovies(params).map { moviesEither ->
            moviesEither.mapLeft(SuggestionError::Source).notEmpty()
        }

    private fun Either<SuggestionError, List<Movie>>.notEmpty(): Either<SuggestionError, NonEmptyList<Movie>> =
        flatMap { list ->
            NonEmptyList.fromList(list)
                .toEither { SuggestionError.NoSuggestions }
        }
}
