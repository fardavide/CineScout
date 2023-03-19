package cinescout.suggestions.data

import arrow.core.Either
import arrow.core.Nel
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.suggestions.domain.SuggestionRepository
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedScreenplay
import cinescout.suggestions.domain.model.SuggestedScreenplayId
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.SuggestionError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class RealSuggestionRepository(
    private val localSuggestionDataSource: LocalSuggestionDataSource
) : SuggestionRepository {

    override fun getSuggestionIds(
        type: ScreenplayType
    ): Flow<Either<SuggestionError, Nel<SuggestedScreenplayId>>> =
        localSuggestionDataSource.findAllSuggestionIds(type).map { either ->
            either.mapLeft { SuggestionError.NoSuggestions }
        }

    override suspend fun storeSuggestionIds(ids: Collection<SuggestedScreenplayId>) {
        localSuggestionDataSource.insertSuggestionIds(ids)
    }

    override suspend fun storeSuggestions(screenplays: Collection<SuggestedScreenplay>) {
        val movies = screenplays.filterIsInstance<SuggestedMovie>()
        localSuggestionDataSource.insertSuggestedMovies(movies)
        val tvShows = screenplays.filterIsInstance<SuggestedTvShow>()
        localSuggestionDataSource.insertSuggestedTvShows(tvShows)
    }
}
