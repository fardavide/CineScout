package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.continuations.either
import arrow.core.flatMap
import arrow.core.getOrHandle
import arrow.core.left
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.SuggestionError
import cinescout.movies.domain.usecase.GetAllDislikedMovies
import cinescout.movies.domain.usecase.GetAllLikedMovies
import cinescout.movies.domain.usecase.GetAllRatedMovies
import cinescout.utils.kotlin.nonEmpty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map

class GetSuggestedMovies(
    private val buildDiscoverMoviesParams: BuildDiscoverMoviesParams,
    private val getAllDislikedMovies: GetAllDislikedMovies,
    private val getAllLikedMovies: GetAllLikedMovies,
    private val getAllRatedMovies: GetAllRatedMovies,
    private val movieRepository: MovieRepository
) {

    operator fun invoke(): Flow<Either<SuggestionError, NonEmptyList<Movie>>> =
        combineTransform(
            getAllDislikedMovies(),
            getAllLikedMovies(),
            getAllRatedMovies().loadAll()
        ) { dislikedEither, likedEither, ratedEither ->
            either {
                val disliked = dislikedEither
                    .getOrHandle { emptyList() }
                val liked = likedEither
                    .getOrHandle { emptyList() }
                val rated = ratedEither
                    .mapLeft(SuggestionError::Source)
                    .bind()

                val params = buildDiscoverMoviesParams(rated.data)
                    .bind()

                val allKnownMovies = disliked + liked + rated.data.map { it.movie }
                discoverMovies(params).filterKnownMovies(allKnownMovies)
            }.fold(
                ifLeft = { emit(it.left()) },
                ifRight = { emitAll(it) }
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
