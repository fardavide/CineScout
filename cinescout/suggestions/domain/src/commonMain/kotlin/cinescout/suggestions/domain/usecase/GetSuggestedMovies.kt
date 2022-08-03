package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.continuations.either
import arrow.core.flatMap
import arrow.core.left
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.SuggestionError
import cinescout.movies.domain.usecase.GetAllKnownMovies
import cinescout.utils.kotlin.combineToPair
import cinescout.utils.kotlin.nonEmpty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetSuggestedMovies(
    private val buildDiscoverMoviesParams: BuildDiscoverMoviesParams,
    private val getAllKnownMovies: GetAllKnownMovies,
    private val movieRepository: MovieRepository
) {

    operator fun invoke(): Flow<Either<SuggestionError, NonEmptyList<Movie>>> =
        combineToPair(
            buildDiscoverMoviesParams(),
            getAllKnownMovies().loadAll().distinctUntilChanged()
        ).flatMapLatest { (paramsEither, allKnownMoviesEither) ->
            either {
                val params = paramsEither
                    .bind()
                val allKnownMovies = allKnownMoviesEither
                    .mapLeft(SuggestionError::Source)
                    .bind()

                discoverMovies(params).filterKnownMovies(allKnownMovies.data)
            }.fold(
                ifLeft = { suggestionError -> flowOf(suggestionError.left()) },
                ifRight = { flow -> flow }
            )
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

    private fun Flow<Either<SuggestionError, NonEmptyList<Movie>>>.filterKnownMovies(
        knownMovies: List<Movie>
    ): Flow<Either<SuggestionError, NonEmptyList<Movie>>> =
        map { listEither ->
            listEither.flatMap { list ->
                list.filterNot { movie -> movie in knownMovies }
                    .nonEmpty { SuggestionError.NoSuggestions }
            }
        }
}
