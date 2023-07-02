package cinescout.history.data.local.mapper

import cinescout.database.model.DatabaseHistory
import cinescout.database.model.id.DatabaseTraktScreenplayId
import cinescout.history.domain.model.MovieHistory
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.history.domain.model.ScreenplayHistoryItem
import cinescout.history.domain.model.TvShowHistory
import cinescout.screenplay.data.local.mapper.toTmdbDatabaseId
import cinescout.screenplay.data.local.mapper.toTraktDatabaseId
import cinescout.screenplay.domain.model.EpisodeNumber
import cinescout.screenplay.domain.model.SeasonNumber
import cinescout.screenplay.domain.model.id.MovieIds
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TvShowIds
import org.koin.core.annotation.Factory

@Factory
internal class DatabaseScreenplayHistoryMapper {

    fun toDatabaseModels(history: ScreenplayHistory): List<DatabaseHistory> =
        toDatabaseModels(history.screenplayIds, history.items)

    private fun toDatabaseModels(screenplayIds: ScreenplayIds, history: List<ScreenplayHistoryItem>) =
        history.map { item ->
            DatabaseHistory(
                itemId = item.id.toDatabaseId(),
                tmdbId = screenplayIds.toTmdbDatabaseId(),
                traktId = screenplayIds.toTraktDatabaseId(),
                watchedAt = item.watchedAt,
                seasonNumber = when (item) {
                    is ScreenplayHistoryItem.Episode -> item.seasonNumber.value
                    is ScreenplayHistoryItem.Movie -> null
                },
                episodeNumber = when (item) {
                    is ScreenplayHistoryItem.Episode -> item.episodeNumber.value
                    is ScreenplayHistoryItem.Movie -> null
                },
                isPlaceholder = 0
            )
        }

    fun toDomainModel(screenplayIds: ScreenplayIds, history: List<DatabaseHistory>): ScreenplayHistory =
        when (screenplayIds) {
            is MovieIds -> toMovieHistory(screenplayIds, history)
            is TvShowIds -> toTvShowHistory(screenplayIds, history)
        }

    private fun toMovieHistory(screenplayIds: MovieIds, history: List<DatabaseHistory>): MovieHistory =
        MovieHistory(
            items = history.map { item ->
                checkId(screenplayIds, item.traktId)
                ScreenplayHistoryItem.Movie(
                    id = item.itemId.toDomainId(),
                    watchedAt = item.watchedAt
                )
            },
            screenplayIds = screenplayIds
        )

    private fun toTvShowHistory(screenplayIds: TvShowIds, history: List<DatabaseHistory>): TvShowHistory =
        TvShowHistory(
            items = history.map { item ->
                checkId(screenplayIds, item.traktId)
                ScreenplayHistoryItem.Episode(
                    id = item.itemId.toDomainId(),
                    watchedAt = item.watchedAt,
                    seasonNumber = SeasonNumber(checkNotNull(item.seasonNumber) { "seasonNumber is null" }),
                    episodeNumber = EpisodeNumber(checkNotNull(item.episodeNumber) { "episodeNumber is null" })
                )
            },
            screenplayIds = screenplayIds
        )

    private fun checkId(domainIds: ScreenplayIds, databaseTraktId: DatabaseTraktScreenplayId) {
        check(domainIds.trakt.value == databaseTraktId.value) {
            "Expected tmdb id to be ${domainIds.trakt.value} but was $databaseTraktId"
        }
    }
}
