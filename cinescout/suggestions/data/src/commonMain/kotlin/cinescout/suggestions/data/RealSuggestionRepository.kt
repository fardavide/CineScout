package cinescout.suggestions.data

import arrow.core.Either
import arrow.core.Nel
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.settings.domain.usecase.GetSuggestionSettings
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedScreenplay
import cinescout.suggestions.domain.model.SuggestedScreenplayId
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.model.SuggestionSourceType
import cinescout.suggestions.domain.repository.SuggestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class RealSuggestionRepository(
    private val localSuggestionDataSource: LocalSuggestionDataSource,
    private val getSuggestionSettings: GetSuggestionSettings
) : SuggestionRepository {

    override fun getSuggestionIds(
        type: ScreenplayTypeFilter
    ): Flow<Either<SuggestionError, Nel<SuggestedScreenplayId>>> =
        getEnabledSuggestionSourceTypes().flatMapLatest { enabledSourceTypes ->
            localSuggestionDataSource.findAllSuggestionIds(type, enabledSourceTypes).map { either ->
                either.mapLeft { SuggestionError.NoSuggestions }
            }
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

    private fun getEnabledSuggestionSourceTypes(): Flow<List<SuggestionSourceType>> =
        getSuggestionSettings().map { suggestionSettings ->
            SuggestionSourceType.values().mapNotNull { sourceType ->
                sourceType.takeIf {
                    when (sourceType) {
                        SuggestionSourceType.Anticipated -> suggestionSettings.isAnticipatedSuggestionsEnabled
                        SuggestionSourceType.InAppGenerated -> suggestionSettings.isInAppGeneratedSuggestionsEnabled
                        SuggestionSourceType.PersonalSuggestions -> suggestionSettings.isPersonalSuggestionsEnabled
                        SuggestionSourceType.Popular -> suggestionSettings.isPopularSuggestionsEnabled
                        SuggestionSourceType.Recommended -> suggestionSettings.isRecommendedSuggestionsEnabled
                        SuggestionSourceType.Trending -> suggestionSettings.isTrendingSuggestionsEnabled
                    }
                }
            }
        }
}
