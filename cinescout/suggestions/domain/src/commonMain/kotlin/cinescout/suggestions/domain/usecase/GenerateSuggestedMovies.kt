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
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.SuggestionError
import cinescout.movies.domain.usecase.GetAllDislikedMovies
import cinescout.movies.domain.usecase.GetAllLikedMovies
import cinescout.movies.domain.usecase.GetAllRatedMovies
import cinescout.movies.domain.usecase.GetAllWatchlistMovies
import cinescout.movies.domain.usecase.GetMovieExtras
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.utils.kotlin.combineLatest
import cinescout.utils.kotlin.nonEmpty
import cinescout.utils.kotlin.shiftWithAnyRight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import store.PagedData
import store.Paging
import store.Refresh

class GenerateSuggestedMovies(
    private val buildDiscoverMoviesParams: BuildDiscoverMoviesParams,
    private val getAllDislikedMovies: GetAllDislikedMovies,
    private val getAllLikedMovies: GetAllLikedMovies,
    private val getAllRatedMovies: GetAllRatedMovies,
    private val getAllWatchlistMovies: GetAllWatchlistMovies,
    private val getMovieExtras: GetMovieExtras,
    private val movieRepository: MovieRepository
) {

    operator fun invoke(
        suggestionsMode: SuggestionsMode
    ): Flow<Either<SuggestionError, NonEmptyList<Movie>>> =
        combineLatest(
            getAllDislikedMovies(),
            getAllLikedMovies(),
            getAllRatedMovies(suggestionsMode),
            getAllWatchlistMovies(suggestionsMode)
        ) { disliked, liked, ratedEither, watchlistEither ->

            val rated = ratedEither
                .fold(
                    ifLeft = { emptyList() },
                    ifRight = { it.data }
                )

            val watchlist = watchlistEither
                .fold(
                    ifLeft = { emptyList() },
                    ifRight = { it.data }
                )

            val dislikedDetails = disliked.map { getMovieExtras(it, refresh = Refresh.IfNeeded).first() }
                .shiftWithAnyRight()
                .getOrHandle { emptyList() }

            val likedDetails = liked.map { getMovieExtras(it, refresh = Refresh.IfNeeded).first() }
                .shiftWithAnyRight()
                .getOrHandle { emptyList() }

            val ratedDetails = rated.map { getMovieExtras(it, refresh = Refresh.IfNeeded).first() }
                .shiftWithAnyRight()
                .getOrHandle { emptyList() }

            val watchlistDetails = watchlist.map { getMovieExtras(it, refresh = Refresh.IfNeeded).first() }
                .shiftWithAnyRight()
                .getOrHandle { emptyList() }

            NonEmptyList.fromList(likedDetails + ratedDetails.filterPositiveRating() + watchlistDetails)
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

    private fun getAllRatedMovies(
        suggestionsMode: SuggestionsMode
    ): Flow<Either<DataError, PagedData<MovieWithPersonalRating, Paging>>> =
        when (suggestionsMode) {
            SuggestionsMode.Deep -> getAllRatedMovies(refresh = Refresh.Once).filterIntermediatePages()
            SuggestionsMode.Quick -> getAllRatedMovies(refresh = Refresh.IfNeeded)
        }

    private fun getAllWatchlistMovies(
        suggestionsMode: SuggestionsMode
    ): Flow<Either<DataError, PagedData<Movie, Paging>>> =
        when (suggestionsMode) {
            SuggestionsMode.Deep -> getAllWatchlistMovies(refresh = Refresh.Once).filterIntermediatePages()
            SuggestionsMode.Quick -> getAllWatchlistMovies(refresh = Refresh.IfNeeded)
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
