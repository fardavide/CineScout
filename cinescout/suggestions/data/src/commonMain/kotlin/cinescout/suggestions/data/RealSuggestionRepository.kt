package cinescout.suggestions.data

import arrow.core.Either
import arrow.core.Nel
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.settings.domain.usecase.GetAppSettings
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
    private val getAppSettings: GetAppSettings
) : SuggestionRepository {

    override fun getSuggestionIds(
        type: ScreenplayType
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
        getAppSettings().map { appSettings ->
            SuggestionSourceType.values().mapNotNull { sourceType ->
                sourceType.takeIf {
                    when (sourceType) {
                        SuggestionSourceType.Anticipated -> appSettings.isAnticipatedSuggestionsEnabled
                        SuggestionSourceType.InAppGenerated -> appSettings.isInAppGeneratedSuggestionsEnabled
                        SuggestionSourceType.PersonalSuggestions -> appSettings.isPersonalSuggestionsEnabled
                        SuggestionSourceType.Popular -> appSettings.isPopularSuggestionsEnabled
                        SuggestionSourceType.Recommended -> appSettings.isRecommendedSuggestionsEnabled
                        SuggestionSourceType.Trending -> appSettings.isTrendingSuggestionsEnabled
                    }
                }
            }
        }
}
