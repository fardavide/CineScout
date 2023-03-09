package cinescout.suggestions.domain

import arrow.core.Either
import arrow.core.Nel
import arrow.core.left
import arrow.core.right
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedMovieId
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.SuggestedTvShowId
import cinescout.suggestions.domain.model.SuggestionError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface SuggestionRepository {

    fun getSuggestedMovieIds(): Flow<Either<SuggestionError, Nel<SuggestedMovieId>>>

    fun getSuggestedTvShowIds(): Flow<Either<SuggestionError, Nel<SuggestedTvShowId>>>

    suspend fun storeSuggestedMovies(movies: Nel<SuggestedMovie>)

    suspend fun storeSuggestedTvShows(tvShows: Nel<SuggestedTvShow>)
}

class FakeSuggestionRepository(
    private val suggestedMovieIds: Nel<SuggestedMovieId>? = null,
    private val suggestedMovieIdsFlow: MutableStateFlow<Either<SuggestionError, Nel<SuggestedMovieId>>> =
        MutableStateFlow(suggestedMovieIds?.right() ?: SuggestionError.NoSuggestions.left()),
    private val suggestedMovies: Nel<SuggestedMovie>? = null,
    private val suggestedMoviesFlow: MutableStateFlow<Either<SuggestionError, Nel<SuggestedMovie>>> =
        MutableStateFlow(suggestedMovies?.right() ?: SuggestionError.NoSuggestions.left()),
    private val suggestedTvShowIds: Nel<SuggestedTvShowId>? = null,
    private val suggestedTvShowIdsFlow: MutableStateFlow<Either<SuggestionError, Nel<SuggestedTvShowId>>> =
        MutableStateFlow(suggestedTvShowIds?.right() ?: SuggestionError.NoSuggestions.left()),
    private val suggestedTvShows: Nel<SuggestedTvShow>? = null,
    private val suggestedTvShowsFlow: MutableStateFlow<Either<SuggestionError, Nel<SuggestedTvShow>>> =
        MutableStateFlow(suggestedTvShows?.right() ?: SuggestionError.NoSuggestions.left())
) : SuggestionRepository {

    override fun getSuggestedMovieIds(): Flow<Either<SuggestionError, Nel<SuggestedMovieId>>> =
        suggestedMovieIdsFlow

    override fun getSuggestedTvShowIds(): Flow<Either<SuggestionError, Nel<SuggestedTvShowId>>> =
        suggestedTvShowIdsFlow

    override suspend fun storeSuggestedMovies(movies: Nel<SuggestedMovie>) {
        val allSuggestionIds = suggestedMovieIdsFlow.value.fold(
            ifLeft = { movies.map { SuggestedMovieId(it.affinity, it.movie.tmdbId, it.source) } },
            ifRight = { it + movies.map { SuggestedMovieId(it.affinity, it.movie.tmdbId, it.source) } }
        )
        suggestedMovieIdsFlow.emit(allSuggestionIds.right())
        val allSuggestions = suggestedMoviesFlow.value.fold(
            ifLeft = { movies },
            ifRight = { it + movies }
        )
        suggestedMoviesFlow.emit(allSuggestions.right())
    }

    override suspend fun storeSuggestedTvShows(tvShows: Nel<SuggestedTvShow>) {
        val allSuggestionIds = suggestedTvShowIdsFlow.value.fold(
            ifLeft = { tvShows.map { SuggestedTvShowId(it.affinity, it.tvShow.tmdbId, it.source) } },
            ifRight = { it + tvShows.map { SuggestedTvShowId(it.affinity, it.tvShow.tmdbId, it.source) } }
        )
        suggestedTvShowIdsFlow.emit(allSuggestionIds.right())
        val allSuggestions = suggestedTvShowsFlow.value.fold(
            ifLeft = { tvShows },
            ifRight = { it + tvShows }
        )
        suggestedTvShowsFlow.emit(allSuggestions.right())
    }
}
