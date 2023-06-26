package cinescout.seasons.data.local.datasource

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import arrow.core.toNonEmptyListOrNull
import cinescout.database.EpisodeQueries
import cinescout.database.SeasonQueries
import cinescout.database.util.suspendTransaction
import cinescout.screenplay.data.local.mapper.toTraktDatabaseId
import cinescout.screenplay.domain.model.id.TvShowIds
import cinescout.seasons.data.datasource.LocalSeasonDataSource
import cinescout.seasons.data.local.mapper.DatabaseEpisodeMapper
import cinescout.seasons.data.local.mapper.DatabaseSeasonMapper
import cinescout.seasons.domain.model.TvShowSeasonsWithEpisodes
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalSeasonDataSource(
    private val episodeMapper: DatabaseEpisodeMapper,
    private val episodeQueries: EpisodeQueries,
    private val seasonMapper: DatabaseSeasonMapper,
    private val seasonQueries: SeasonQueries,
    @Named(IoDispatcher) private val readDispatcher: CoroutineDispatcher,
    private val transacter: Transacter,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
) : LocalSeasonDataSource {

    override fun findSeasonsWithEpisodes(tvShowIds: TvShowIds): Flow<TvShowSeasonsWithEpisodes?> =
        seasonQueries.findAllSeasonsWithEpisodesByTraktTvShowId(tvShowIds.toTraktDatabaseId())
            .asFlow()
            .mapToList(readDispatcher)
            .map { it.toNonEmptyListOrNull()?.let(seasonMapper::toSeasonsWithEpisodes) }

    override suspend fun insertSeasonsWithEpisodes(seasonsWithEpisodes: TvShowSeasonsWithEpisodes) {
        transacter.suspendTransaction(writeDispatcher) {
            for (season in seasonsWithEpisodes.seasonsWithEpisodes) {
                seasonQueries.insert(seasonMapper.toDatabaseModel(season.season, seasonsWithEpisodes.tvShowIds))

                for (episode in season.episodes) {
                    episodeQueries.insert(episodeMapper.toDatabaseModel(episode, season.season.ids))
                }
            }
        }
    }
}
