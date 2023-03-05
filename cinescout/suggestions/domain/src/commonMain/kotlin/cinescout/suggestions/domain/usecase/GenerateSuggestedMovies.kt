package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.flatMap
import arrow.core.getOrElse
import arrow.core.left
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.usecase.GetAllDislikedMovies
import cinescout.movies.domain.usecase.GetAllLikedMovies
import cinescout.movies.domain.usecase.GetAllRatedMovies
import cinescout.movies.domain.usecase.GetAllWatchlistMovies
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionSource
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

interface GenerateSuggestedMovies {

    operator fun invoke(
        suggestionsMode: SuggestionsMode
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedMovie>>>
}

@Factory
class RealGenerateSuggestedMovies(
    private val getAllDislikedMovies: GetAllDislikedMovies,
    private val getAllLikedMovies: GetAllLikedMovies,
    private val getAllRatedMovies: GetAllRatedMovies,
    private val getAllWatchlistMovies: GetAllWatchlistMovies,
    private val movieRepository: MovieRepository
) : GenerateSuggestedMovies {

    override operator fun invoke(
        suggestionsMode: SuggestionsMode
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedMovie>>> = combineLatest(
        getAllDislikedMovies(),
        getAllLikedMovies(),
        getAllRatedMovies(refresh = Refresh.IfNeeded),
        getAllWatchlistMovies(refresh = Refresh.IfNeeded)
    ) { disliked, liked, ratedEither, watchlistEither ->

        val rated = ratedEither
            .getOrElse { emptyList() }

        val watchlist = watchlistEither
            .getOrElse { emptyList() }

        val positiveMovies = liked.withLikedSource() + rated.withRatedSource() + watchlist.withWatchlistSource()
        val (movieId, source) = positiveMovies.randomOrNone().map { it.movie.tmdbId to it.source }
            .getOrElse { return@combineLatest flowOf(SuggestionError.NoSuggestions.left()) }

        getRecommendationsFor(movieId, suggestionsMode).map { dataEither ->
            dataEither.mapLeft {
                SuggestionError.Source(it as DataError.Remote)
            }.flatMap { pagedData ->
                val suggestionsPagedData = pagedData.map { SuggestedMovie(it, source) }
                val allKnownMovies = disliked + liked + rated.map { it.movie } + watchlist
                suggestionsPagedData.data.filterKnownMovies(allKnownMovies)
            }
        }
    }

    private fun getRecommendationsFor(
        movieId: TmdbMovieId,
        suggestionsMode: SuggestionsMode
    ): Flow<Either<DataError, PagedData<Movie, Paging>>> = when (suggestionsMode) {
        SuggestionsMode.Deep ->
            movieRepository.getRecommendationsFor(movieId, refresh = Refresh.Once).filterIntermediatePages()
        SuggestionsMode.Quick ->
            movieRepository.getRecommendationsFor(movieId, refresh = Refresh.IfNeeded)
    }

    private fun List<Movie>.withLikedSource(): List<SuggestedMovie> =
        map { SuggestedMovie(it, SuggestionSource.FromLiked(it.title)) }

    private fun List<Movie>.withWatchlistSource(): List<SuggestedMovie> =
        map { SuggestedMovie(it, SuggestionSource.FromWatchlist(it.title)) }

    private fun List<MovieWithPersonalRating>.withRatedSource(): List<SuggestedMovie> =
        filter { it.personalRating.value >= 6 }
            .map { SuggestedMovie(it.movie, SuggestionSource.FromRated(it.movie.title, it.personalRating)) }

    private fun List<SuggestedMovie>.filterKnownMovies(
        knownMovies: List<Movie>
    ): Either<SuggestionError, NonEmptyList<SuggestedMovie>> {
        val knownMovieIds = knownMovies.map { it.tmdbId }
        return filterNot { suggestedMovie -> suggestedMovie.movie.tmdbId in knownMovieIds }
            .nonEmpty { SuggestionError.NoSuggestions }
    }
}

class FakeGenerateSuggestedMovies(
    private val result: Either<SuggestionError, NonEmptyList<SuggestedMovie>> = SuggestionError.NoSuggestions.left()
) : GenerateSuggestedMovies {

    override operator fun invoke(
        suggestionsMode: SuggestionsMode
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedMovie>>> = flowOf(result)
}
