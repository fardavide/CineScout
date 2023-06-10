package cinescout.seasons.data.datasource

import cinescout.screenplay.domain.model.ids.TvShowIds
import cinescout.seasons.domain.model.TvShowSeasonsWithEpisodes
import kotlinx.coroutines.flow.Flow

interface LocalTvShowSeasonsWithEpisodesDataSource {

    fun findSeasonsWithEpisodes(tvShowIds: TvShowIds): Flow<TvShowSeasonsWithEpisodes?>

    suspend fun insertSeasonsWithEpisodes(seasonsWithEpisodes: TvShowSeasonsWithEpisodes)
}
