package cinescout.suggestions.data

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptyList
import arrow.core.toNonEmptyListOrNone
import cinescout.error.DataError
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedTvShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface LocalSuggestionDataSource {

    fun findAllSuggestedMovies(): Flow<Either<DataError.Local, NonEmptyList<SuggestedMovie>>>

    fun findAllSuggestedTvShows(): Flow<Either<DataError.Local, NonEmptyList<SuggestedTvShow>>>

    suspend fun insertSuggestedMovies(suggestedMovies: Collection<SuggestedMovie>)

    suspend fun insertSuggestedTvShows(suggestedTvShows: Collection<SuggestedTvShow>)
}

class FakeLocalSuggestionDataSource(
    private val suggestedMovies: Nel<SuggestedMovie>? = null,
    private val suggestedTvShows: Nel<SuggestedTvShow>? = null
) : LocalSuggestionDataSource {

    private val mutableSuggestedMoviesFlow = MutableStateFlow(suggestedMovies.orEmpty())
    private val mutableSuggestedTvShowsFlow = MutableStateFlow(suggestedTvShows.orEmpty())

    override fun findAllSuggestedMovies(): Flow<Either<DataError.Local, NonEmptyList<SuggestedMovie>>> =
        mutableSuggestedMoviesFlow.map { list ->
            list.toNonEmptyListOrNone()
                .toEither { DataError.Local.NoCache }
        }

    override fun findAllSuggestedTvShows(): Flow<Either<DataError.Local, NonEmptyList<SuggestedTvShow>>> =
        mutableSuggestedTvShowsFlow.map { list ->
            list.toNonEmptyListOrNone()
                .toEither { DataError.Local.NoCache }
        }

    override suspend fun insertSuggestedMovies(suggestedMovies: Collection<SuggestedMovie>) {
        mutableSuggestedMoviesFlow.emit(mutableSuggestedMoviesFlow.value + suggestedMovies)
    }

    override suspend fun insertSuggestedTvShows(suggestedTvShows: Collection<SuggestedTvShow>) {
        mutableSuggestedTvShowsFlow.emit(mutableSuggestedTvShowsFlow.value + suggestedTvShows)
    }
}
