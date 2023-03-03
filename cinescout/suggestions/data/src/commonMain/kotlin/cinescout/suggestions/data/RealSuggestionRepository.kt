package cinescout.suggestions.data

import arrow.core.Either
import arrow.core.Nel
import cinescout.suggestions.domain.SuggestionRepository
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.SuggestionError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class RealSuggestionRepository(
    private val localSuggestionDataSource: LocalSuggestionDataSource
) : SuggestionRepository {

    override fun getSuggestedMovies(): Flow<Either<SuggestionError, Nel<SuggestedMovie>>> =
        localSuggestionDataSource.findAllSuggestedMovies().map { either ->
            either.mapLeft { SuggestionError.NoSuggestions }
        }

    override fun getSuggestedTvShows(): Flow<Either<SuggestionError, Nel<SuggestedTvShow>>> =
        localSuggestionDataSource.findAllSuggestedTvShows().map { either ->
            either.mapLeft { SuggestionError.NoSuggestions }
        }

    override suspend fun storeSuggestedMovies(movies: Nel<SuggestedMovie>) {
        localSuggestionDataSource.insertSuggestedMovies(movies)
    }

    override suspend fun storeSuggestedTvShows(tvShows: Nel<SuggestedTvShow>) {
        localSuggestionDataSource.insertSuggestedTvShows(tvShows)
    }
}
