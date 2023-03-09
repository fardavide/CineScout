package cinescout.suggestions.data

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptyList
import arrow.core.toNonEmptyListOrNone
import arrow.core.toNonEmptyListOrNull
import cinescout.error.DataError
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedMovieId
import cinescout.suggestions.domain.model.SuggestedScreenplayId
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.SuggestedTvShowId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface LocalSuggestionDataSource {

    fun findAllSuggestedMovieIds(): Flow<Either<DataError.Local, NonEmptyList<SuggestedMovieId>>>

    fun findAllSuggestedTvShowIds(): Flow<Either<DataError.Local, NonEmptyList<SuggestedTvShowId>>>

    suspend fun insertSuggestionIds(suggestions: Collection<SuggestedScreenplayId>)

    suspend fun insertSuggestedMovies(suggestedMovies: Collection<SuggestedMovie>)

    suspend fun insertSuggestedTvShows(suggestedTvShows: Collection<SuggestedTvShow>)
}

class FakeLocalSuggestionDataSource(
    suggestedMovieIds: Nel<SuggestedMovieId>? = null,
    suggestedMovies: Nel<SuggestedMovie>? = null,
    suggestedTvShowIds: Nel<SuggestedTvShowId>? = null,
    suggestedTvShows: Nel<SuggestedTvShow>? = null
) : LocalSuggestionDataSource {

    private val mutableSuggestedMovieIdsFlow = MutableStateFlow(suggestedMovieIds.orEmpty())
    private val mutableSuggestedMoviesFlow = MutableStateFlow(suggestedMovies.orEmpty())
    private val mutableSuggestedTvShowIdsFlow = MutableStateFlow(suggestedTvShowIds.orEmpty())
    private val mutableSuggestedTvShowsFlow = MutableStateFlow(suggestedTvShows.orEmpty())

    override fun findAllSuggestedMovieIds(): Flow<Either<DataError.Local, NonEmptyList<SuggestedMovieId>>> =
        mutableSuggestedMovieIdsFlow.map { list ->
            list.toNonEmptyListOrNone()
                .toEither { DataError.Local.NoCache }
        }

    override fun findAllSuggestedTvShowIds(): Flow<Either<DataError.Local, NonEmptyList<SuggestedTvShowId>>> =
        mutableSuggestedTvShowIdsFlow.map { list ->
            list.toNonEmptyListOrNone()
                .toEither { DataError.Local.NoCache }
        }

    override suspend fun insertSuggestionIds(suggestions: Collection<SuggestedScreenplayId>) {
        suggestions.filterIsInstance<SuggestedMovieId>().toNonEmptyListOrNull()?.let {
            mutableSuggestedMovieIdsFlow.emit(mutableSuggestedMovieIdsFlow.value + it)
        }
        suggestions.filterIsInstance<SuggestedTvShowId>().toNonEmptyListOrNull()?.let {
            mutableSuggestedTvShowIdsFlow.emit(mutableSuggestedTvShowIdsFlow.value + it)
        }
    }

    override suspend fun insertSuggestedMovies(suggestedMovies: Collection<SuggestedMovie>) {
        mutableSuggestedMovieIdsFlow.emit(
            mutableSuggestedMovieIdsFlow.value + suggestedMovies.map {
                SuggestedMovieId(it.affinity, it.movie.tmdbId, it.source)
            }
        )
        mutableSuggestedMoviesFlow.emit(mutableSuggestedMoviesFlow.value + suggestedMovies)
    }

    override suspend fun insertSuggestedTvShows(suggestedTvShows: Collection<SuggestedTvShow>) {
        mutableSuggestedTvShowIdsFlow.emit(
            mutableSuggestedTvShowIdsFlow.value + suggestedTvShows.map {
                SuggestedTvShowId(
                    it.affinity,
                    it.tvShow.tmdbId,
                    it.source
                )
            }
        )
        mutableSuggestedTvShowsFlow.emit(mutableSuggestedTvShowsFlow.value + suggestedTvShows)
    }
}
