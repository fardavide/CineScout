package cinescout.suggestions.data

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptyList
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedTvShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

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

    override fun findAllSuggestedMovies(): Flow<Either<DataError.Local, NonEmptyList<SuggestedMovie>>> =
        flowOf(suggestedMovies?.right() ?: DataError.Local.NoCache.left())

    override fun findAllSuggestedTvShows(): Flow<Either<DataError.Local, NonEmptyList<SuggestedTvShow>>> =
        flowOf(suggestedTvShows?.right() ?: DataError.Local.NoCache.left())

    override suspend fun insertSuggestedMovies(suggestedMovies: Collection<SuggestedMovie>) {

    }

    override suspend fun insertSuggestedTvShows(suggestedTvShows: Collection<SuggestedTvShow>) {

    }
}
