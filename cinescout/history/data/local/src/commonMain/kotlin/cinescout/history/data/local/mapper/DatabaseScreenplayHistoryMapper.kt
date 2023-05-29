package cinescout.history.data.local.mapper

import cinescout.database.model.DatabaseHistory
import cinescout.database.model.DatabaseTraktScreenplayId
import cinescout.history.domain.model.MovieHistory
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.history.domain.model.ScreenplayHistoryItem
import cinescout.history.domain.model.TvShowHistory
import cinescout.history.domain.model.TvShowHistoryState
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.domain.model.EpisodeNumber
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.SeasonNumber
import org.koin.core.annotation.Factory

@Factory
internal class DatabaseScreenplayHistoryMapper {

    fun toDatabaseModels(history: ScreenplayHistory): List<DatabaseHistory> = history.items.map { item ->
        DatabaseHistory(
            itemId = item.id.toDatabaseId(),
            tmdbId = history.screenplayIds.tmdb.toDatabaseId(),
            traktId = history.screenplayIds.trakt.toDatabaseId(),
            watchedAt = item.watchedAt,
            seasonNumber = when (item) {
                is ScreenplayHistoryItem.Episode -> item.seasonNumber.value
                is ScreenplayHistoryItem.Movie -> null
            },
            episodeNumber = when (item) {
                is ScreenplayHistoryItem.Episode -> item.episodeNumber.value
                is ScreenplayHistoryItem.Movie -> null
            }
        )
    }

    fun toDomainModel(screenplayIds: ScreenplayIds, history: List<DatabaseHistory>): ScreenplayHistory =
        when (screenplayIds) {
            is ScreenplayIds.Movie -> toMovieHistory(screenplayIds, history)
            is ScreenplayIds.TvShow -> toTvShowHistory(screenplayIds, history)
        }

    private fun toMovieHistory(
        screenplayIds: ScreenplayIds.Movie,
        history: List<DatabaseHistory>
    ): MovieHistory = MovieHistory(
        items = history.map { item ->
            checkId(screenplayIds, item.traktId)
            ScreenplayHistoryItem.Movie(
                id = item.itemId.toDomainId(),
                watchedAt = item.watchedAt
            )
        },
        screenplayIds = screenplayIds
    )

    private fun toTvShowHistory(
        screenplayIds: ScreenplayIds.TvShow,
        history: List<DatabaseHistory>
    ): TvShowHistory = TvShowHistory(
        items = history.map { item ->
            checkId(screenplayIds, item.traktId)
            ScreenplayHistoryItem.Episode(
                id = item.itemId.toDomainId(),
                watchedAt = item.watchedAt,
                seasonNumber = SeasonNumber(checkNotNull(item.seasonNumber) { "seasonNumber is null" }),
                episodeNumber = EpisodeNumber(checkNotNull(item.episodeNumber) { "episodeNumber is null" })
            )
        },
        screenplayIds = screenplayIds,
        // TODO: Determine if InProgress or Completed
        state = if (history.isNotEmpty()) TvShowHistoryState.InProgress else TvShowHistoryState.Unwatched
    )

    private fun checkId(domainIds: ScreenplayIds, databaseTraktId: DatabaseTraktScreenplayId) {
        check(domainIds.tmdb.value == databaseTraktId.value) {
            "Expected tmdb id to be ${domainIds.tmdb.value} but was $databaseTraktId"
        }
    }
}
