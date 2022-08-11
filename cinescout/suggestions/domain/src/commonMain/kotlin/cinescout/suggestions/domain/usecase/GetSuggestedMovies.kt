package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.flatMap
import arrow.core.getOrHandle
import arrow.core.left
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithExtras
import cinescout.movies.domain.model.SuggestionError
import cinescout.movies.domain.usecase.GetAllDislikedMovies
import cinescout.movies.domain.usecase.GetAllLikedMovies
import cinescout.movies.domain.usecase.GetAllRatedMovies
import cinescout.movies.domain.usecase.GetMovieExtras
import cinescout.store.Refresh
import cinescout.utils.kotlin.combineLatest
import cinescout.utils.kotlin.combineToList
import cinescout.utils.kotlin.nonEmpty
import cinescout.utils.kotlin.shiftWithAnyRight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetSuggestedMovies(
    private val buildDiscoverMoviesParams: BuildDiscoverMoviesParams,
    private val getAllDislikedMovies: GetAllDislikedMovies,
    private val getAllLikedMovies: GetAllLikedMovies,
    private val getAllRatedMovies: GetAllRatedMovies,
    private val getMovieExtras: GetMovieExtras,
    private val movieRepository: MovieRepository
) {

    operator fun invoke(): Flow<Either<SuggestionError, NonEmptyList<Movie>>> =
        combineLatest(
            getAllDislikedMovies(),
            getAllLikedMovies(),
            getAllRatedMovies().loadAll()
        ) { dislikedEither, likedEither, ratedEither ->

            val disliked = dislikedEither
                .getOrHandle { emptyList() }

            val liked = likedEither
                .getOrHandle { emptyList() }

            val rated = ratedEither
                .fold(
                    ifLeft = { emptyList() },
                    ifRight = { it.data }
                )

            // TODO
            val watchlist = emptyList<Movie>()

            combineLatest(
                disliked.map { getMovieExtras(it, Refresh.IfNeeded) }.combineToList(),
                liked.map { getMovieExtras(it, Refresh.IfNeeded) }.combineToList(),
                rated.map { getMovieExtras(it, Refresh.IfNeeded) }.combineToList(),
                watchlist.map { getMovieExtras(it, Refresh.IfNeeded) }.combineToList()
            ) { dislikedDetailsEither, likedDetailsEither, ratedDetailsEither, watchlistDetailsEither ->

                val dislikedDetails = dislikedDetailsEither
                    .shiftWithAnyRight()
                    .getOrHandle { emptyList() }

                val likedDetails = likedDetailsEither
                    .shiftWithAnyRight()
                    .getOrHandle { emptyList() }

                val ratedDetails = ratedDetailsEither
                    .shiftWithAnyRight()
                    .getOrHandle { emptyList() }

                val watchlistDetails = watchlistDetailsEither
                    .shiftWithAnyRight()
                    .getOrHandle { emptyList() }

                NonEmptyList.fromList(ratedDetails.filterPositiveRating() + watchlistDetails)
                    .toEither { SuggestionError.NoSuggestions }
                    .map { positiveMovies -> buildDiscoverMoviesParams(positiveMovies) }
                    .fold(
                        ifLeft = { noSuggestions -> flowOf(noSuggestions.left()) },
                        ifRight = { params ->
                            val flow = discoverMovies(params).map { listEither ->
                                val allKnownMovies = dislikedDetails + likedDetails + ratedDetails + watchlistDetails
                                listEither.filterKnownMovies(allKnownMovies)
                            }
                            flow
                        }
                    )
            }
        }

    private fun discoverMovies(params: DiscoverMoviesParams): Flow<Either<SuggestionError, NonEmptyList<Movie>>> =
        movieRepository.discoverMovies(params).map { moviesEither ->
            moviesEither.mapLeft {
                when (it) {
                    DataError.Local.NoCache -> SuggestionError.NoSuggestions
                    is DataError.Remote -> SuggestionError.Source(it)
                }
            }.notEmpty()
        }

    private fun List<MovieWithExtras>.filterPositiveRating(): List<MovieWithExtras> =
        filter { movieWithDetails ->
            movieWithDetails.personalRating.fold(
                ifSome = { rating -> rating.value >= 7 },
                ifEmpty = { false }
            )
        }

    private fun Either<SuggestionError, List<Movie>>.notEmpty(): Either<SuggestionError, NonEmptyList<Movie>> =
        flatMap { list ->
            NonEmptyList.fromList(list)
                .toEither { SuggestionError.NoSuggestions }
        }

    private fun Either<SuggestionError, NonEmptyList<Movie>>.filterKnownMovies(
        knownMovies: List<MovieWithExtras>
    ): Either<SuggestionError, NonEmptyList<Movie>> {
        val knownMovieIds = knownMovies.map { it.movieWithDetails.movie.tmdbId }
        return flatMap { list ->
            list.filterNot { movie -> movie.tmdbId in knownMovieIds }
                .nonEmpty { SuggestionError.NoSuggestions }
        }
    }

}
