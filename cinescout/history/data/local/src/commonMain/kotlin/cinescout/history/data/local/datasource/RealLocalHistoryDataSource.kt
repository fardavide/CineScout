package cinescout.history.data.local.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cinescout.database.HistoryQueries
import cinescout.database.util.suspendTransaction
import cinescout.history.data.datasource.LocalHistoryDataSource
import cinescout.history.data.local.mapper.DatabaseScreenplayHistoryMapper
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.screenplay.data.local.mapper.toStringDatabaseId
import cinescout.screenplay.data.local.mapper.toTmdbDatabaseId
import cinescout.screenplay.data.local.mapper.toTraktDatabaseId
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.SeasonAndEpisodeNumber
import cinescout.screenplay.domain.model.id.MovieIds
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TvShowIds
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalHistoryDataSource(
    private val historyMapper: DatabaseScreenplayHistoryMapper,
    private val historyQueries: HistoryQueries,
    @Named(IoDispatcher) private val readDispatcher: CoroutineDispatcher,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
) : LocalHistoryDataSource {

    override suspend fun deleteAll(screenplayId: ScreenplayIds) {
        historyQueries.suspendTransaction(writeDispatcher) {
            when (screenplayId) {
                is MovieIds -> deleteAllByMovieTraktId(screenplayId.trakt.toStringDatabaseId())
                is TvShowIds -> deleteAllByTvShowTraktId(screenplayId.trakt.toStringDatabaseId())
            }
        }
    }

    override fun find(screenplayIds: ScreenplayIds): Flow<ScreenplayHistory> = when (screenplayIds) {
        is MovieIds -> historyQueries.findAllByMovieTraktId(screenplayIds.trakt.toStringDatabaseId())
        is TvShowIds -> historyQueries.findAllByTvShowTraktId(screenplayIds.trakt.toStringDatabaseId())
    }.asFlow()
        .mapToList(readDispatcher)
        .map { historyMapper.toDomainModel(screenplayIds, it) }

    override suspend fun insert(history: ScreenplayHistory) {
        historyQueries.suspendTransaction(writeDispatcher) {
            val databaseModels = historyMapper.toDatabaseModels(history)
            for (databaseModel in databaseModels) {
                insert(databaseModel)
            }
            deletePlaceholders()
        }
    }

    override suspend fun insertPlaceholder(movieIds: MovieIds) {
        historyQueries.suspendTransaction(writeDispatcher) {
            insertMoviePlaceholder(
                traktId = movieIds.toTraktDatabaseId(),
                tmdbId = movieIds.toTmdbDatabaseId()
            )
        }
    }

    override suspend fun insertPlaceholders(tvShowIds: TvShowIds, episodes: List<SeasonAndEpisodeNumber>) {
        val tmdbTvShowId = tvShowIds.toTmdbDatabaseId()
        val traktTvShowId = tvShowIds.toTraktDatabaseId()
        historyQueries.suspendTransaction(writeDispatcher) {
            for (episode in episodes) {
                insertEpisodePlaceholder(
                    traktId = traktTvShowId,
                    tmdbId = tmdbTvShowId,
                    seasonNumber = episode.season.value,
                    episodeNumber = episode.episode.value
                )
            }
        }
    }

    override suspend fun updateAll(histories: List<ScreenplayHistory>, type: ScreenplayTypeFilter) {
        historyQueries.suspendTransaction(writeDispatcher) {
            when (type) {
                ScreenplayTypeFilter.All -> deleteAll()
                ScreenplayTypeFilter.Movies -> deleteAllMovies()
                ScreenplayTypeFilter.TvShows -> deleteAllTvShows()
            }
            for (history in histories) {
                val databaseModels = historyMapper.toDatabaseModels(history)
                for (databaseModel in databaseModels) {
                    insert(databaseModel)
                }
            }
        }
    }
}
