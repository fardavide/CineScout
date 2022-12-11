package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.flatMap
import arrow.core.getOrElse
import arrow.core.left
import cinescout.common.model.SuggestionError
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.usecase.GetAllDislikedMovies
import cinescout.movies.domain.usecase.GetAllLikedMovies
import cinescout.movies.domain.usecase.GetAllRatedMovies
import cinescout.movies.domain.usecase.GetAllWatchlistMovies
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.utils.kotlin.combineLatest
import cinescout.utils.kotlin.nonEmpty
import cinescout.utils.kotlin.randomOrNone
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import store.PagedData
import store.Paging
import store.Refresh

@Factory
class GenerateSuggestedMovies(
    private val getAllDislikedMovies: GetAllDislikedMovies,
    private val getAllLikedMovies: GetAllLikedMovies,
    private val getAllRatedMovies: GetAllRatedMovies,
    private val getAllWatchlistMovies: GetAllWatchlistMovies,
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

            val positiveMovies = liked + rated.filterPositiveRating() + watchlist
            val movieId = positiveMovies.randomOrNone().map { it.tmdbId }
                .getOrElse { return@combineLatest flowOf(SuggestionError.NoSuggestions.left()) }

            getRecommendationsFor(movieId, suggestionsMode).map { dataEither ->
                dataEither.mapLeft {
                    SuggestionError.Source(it as DataError.Remote)
                }.flatMap { pagedData ->
                    val allKnownMovies = disliked + liked + rated.map { it.movie } + watchlist
                    pagedData.data.filterKnownMovies(allKnownMovies)
                }
            }
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

    private fun getRecommendationsFor(
        movieId: TmdbMovieId,
        suggestionsMode: SuggestionsMode
    ): Flow<Either<DataError, PagedData<Movie, Paging>>> =
        when (suggestionsMode) {
            SuggestionsMode.Deep ->
                movieRepository.getRecommendationsFor(movieId, refresh = Refresh.Once).filterIntermediatePages()
            SuggestionsMode.Quick ->
                movieRepository.getRecommendationsFor(movieId, refresh = Refresh.IfNeeded)
        }

    private fun List<MovieWithPersonalRating>.filterPositiveRating(): List<Movie> =
        filter { movieWithPersonalRating -> movieWithPersonalRating.personalRating.value >= 7 }
            .map { it.movie }

    private fun List<Movie>.filterKnownMovies(
        knownMovies: List<Movie>
    ): Either<SuggestionError, NonEmptyList<Movie>> {
        val knownMovieIds = knownMovies.map { it.tmdbId }
        return filterNot { movie -> movie.tmdbId in knownMovieIds }
            .nonEmpty { SuggestionError.NoSuggestions }
    }
}
