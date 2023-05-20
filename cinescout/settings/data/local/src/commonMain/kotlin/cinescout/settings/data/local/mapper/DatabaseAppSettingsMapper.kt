package cinescout.settings.data.local.mapper

import arrow.core.Option
import arrow.core.continuations.option
import cinescout.database.model.DatabaseAppSettings
import cinescout.database.model.DatabaseListFilter
import cinescout.database.model.DatabaseListSorting
import cinescout.database.model.DatabaseListType
import cinescout.settings.domain.model.AppSettings
import cinescout.settings.domain.model.SavedListOptions
import cinescout.settings.domain.model.SuggestionSettings
import org.koin.core.annotation.Factory

@Factory
internal class DatabaseAppSettingsMapper {

    fun toDomainModel(
        @Suppress("UNUSED_PARAMETER") id: Long,
        anticipatedSuggestionsEnabled: Boolean,
        inAppSuggestionsEnabled: Boolean,
        personalSuggestionsEnabled: Boolean,
        popularSuggestionsEnabled: Boolean,
        recommendedSuggestionsEnabled: Boolean,
        trendingSuggestionsEnabled: Boolean,
        savedListFilter: DatabaseListFilter?,
        savedListSorting: DatabaseListSorting?,
        savedListType: DatabaseListType?
    ) = AppSettings(
        savedListOptions = toDomainSavedListOptions(
            filter = savedListFilter,
            sorting = savedListSorting,
            type = savedListType
        ),
        suggestionSettings = toDomainSuggestionSettings(
            anticipatedSuggestionsEnabled = anticipatedSuggestionsEnabled,
            inAppSuggestionsEnabled = inAppSuggestionsEnabled,
            personalSuggestionsEnabled = personalSuggestionsEnabled,
            popularSuggestionsEnabled = popularSuggestionsEnabled,
            recommendedSuggestionsEnabled = recommendedSuggestionsEnabled,
            trendingSuggestionsEnabled = trendingSuggestionsEnabled
        )
    )
    
    fun toDomainModel(appSettings: DatabaseAppSettings) = AppSettings(
        savedListOptions = toDomainSavedListOptions(
            filter = appSettings.savedListFilter,
            sorting = appSettings.savedListSorting,
            type = appSettings.savedListType
        ),
        suggestionSettings = toDomainSuggestionSettings(
            anticipatedSuggestionsEnabled = appSettings.anticipatedSuggestionsEnabled,
            inAppSuggestionsEnabled = appSettings.inAppSuggestionsEnabled,
            personalSuggestionsEnabled = appSettings.personalSuggestionsEnabled,
            popularSuggestionsEnabled = appSettings.popularSuggestionsEnabled,
            recommendedSuggestionsEnabled = appSettings.recommendedSuggestionsEnabled,
            trendingSuggestionsEnabled = appSettings.trendingSuggestionsEnabled
        )
    )

    fun toDatabaseModel(appSettings: AppSettings) = DatabaseAppSettings(
        id = 1,

        anticipatedSuggestionsEnabled = appSettings.suggestionSettings.isAnticipatedSuggestionsEnabled,
        inAppSuggestionsEnabled = appSettings.suggestionSettings.isInAppGeneratedSuggestionsEnabled,
        personalSuggestionsEnabled = appSettings.suggestionSettings.isPersonalSuggestionsEnabled,
        popularSuggestionsEnabled = appSettings.suggestionSettings.isPopularSuggestionsEnabled,
        recommendedSuggestionsEnabled = appSettings.suggestionSettings.isRecommendedSuggestionsEnabled,
        trendingSuggestionsEnabled = appSettings.suggestionSettings.isTrendingSuggestionsEnabled,

        savedListFilter = toDatabaseListFilter(appSettings.savedListOptions),
        savedListSorting = toDatabaseListSorting(appSettings.savedListOptions),
        savedListType = toDatabaseListType(appSettings.savedListOptions)
    )

    private fun toDatabaseListFilter(option: Option<SavedListOptions>): DatabaseListFilter? =
        option.map { options ->
            when (options.filter) {
                SavedListOptions.Filter.Disliked -> DatabaseListFilter.Disliked
                SavedListOptions.Filter.Liked -> DatabaseListFilter.Liked
                SavedListOptions.Filter.Rated -> DatabaseListFilter.Rated
                SavedListOptions.Filter.Watchlist -> DatabaseListFilter.Watchlist
            }
        }.orNull()

    private fun toDatabaseListSorting(option: Option<SavedListOptions>): DatabaseListSorting? =
        option.map { options ->
            when (options.sorting) {
                SavedListOptions.Sorting.ReleaseDateAscending -> DatabaseListSorting.ReleaseDateAscending
                SavedListOptions.Sorting.ReleaseDateDescending -> DatabaseListSorting.ReleaseDateDescending
                SavedListOptions.Sorting.RatingAscending -> DatabaseListSorting.RatingAscending
                SavedListOptions.Sorting.RatingDescending -> DatabaseListSorting.RatingDescending
            }
        }.orNull()

    private fun toDatabaseListType(option: Option<SavedListOptions>): DatabaseListType? =
        option.map { options ->
            when (options.type) {
                SavedListOptions.Type.All -> DatabaseListType.All
                SavedListOptions.Type.Movies -> DatabaseListType.Movies
                SavedListOptions.Type.TvShows -> DatabaseListType.TvShows
            }
        }.orNull()

    private fun toDomainSavedListOptions(
        filter: DatabaseListFilter?,
        sorting: DatabaseListSorting?,
        type: DatabaseListType?
    ): Option<SavedListOptions> {
        return option.eager {
            SavedListOptions(
                filter = Option.fromNullable(toDomainListFilter(filter)).bind(),
                sorting = Option.fromNullable(toDomainListSorting(sorting)).bind(),
                type = Option.fromNullable(toDomainListType(type)).bind()
            )
        }
    }

    private fun toDomainListFilter(filter: DatabaseListFilter?): SavedListOptions.Filter? = when (filter) {
        DatabaseListFilter.Disliked -> SavedListOptions.Filter.Disliked
        DatabaseListFilter.Liked -> SavedListOptions.Filter.Liked
        DatabaseListFilter.Rated -> SavedListOptions.Filter.Rated
        DatabaseListFilter.Watchlist -> SavedListOptions.Filter.Watchlist
        null -> null
    }

    private fun toDomainListSorting(sorting: DatabaseListSorting?): SavedListOptions.Sorting? =
        when (sorting) {
            DatabaseListSorting.ReleaseDateAscending -> SavedListOptions.Sorting.ReleaseDateAscending
            DatabaseListSorting.ReleaseDateDescending -> SavedListOptions.Sorting.ReleaseDateDescending
            DatabaseListSorting.RatingAscending -> SavedListOptions.Sorting.RatingAscending
            DatabaseListSorting.RatingDescending -> SavedListOptions.Sorting.RatingDescending
            null -> null
        }

    private fun toDomainListType(type: DatabaseListType?): SavedListOptions.Type? = when (type) {
        DatabaseListType.All -> SavedListOptions.Type.All
        DatabaseListType.Movies -> SavedListOptions.Type.Movies
        DatabaseListType.TvShows -> SavedListOptions.Type.TvShows
        null -> null
    }

    private fun toDomainSuggestionSettings(
        anticipatedSuggestionsEnabled: Boolean,
        inAppSuggestionsEnabled: Boolean,
        personalSuggestionsEnabled: Boolean,
        popularSuggestionsEnabled: Boolean,
        recommendedSuggestionsEnabled: Boolean,
        trendingSuggestionsEnabled: Boolean
    ) = SuggestionSettings(
        isAnticipatedSuggestionsEnabled = anticipatedSuggestionsEnabled,
        isInAppGeneratedSuggestionsEnabled = inAppSuggestionsEnabled,
        isPersonalSuggestionsEnabled = personalSuggestionsEnabled,
        isPopularSuggestionsEnabled = popularSuggestionsEnabled,
        isRecommendedSuggestionsEnabled = recommendedSuggestionsEnabled,
        isTrendingSuggestionsEnabled = trendingSuggestionsEnabled
    )
}
