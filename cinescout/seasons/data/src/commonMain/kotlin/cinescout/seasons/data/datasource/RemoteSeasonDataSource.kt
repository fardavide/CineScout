package cinescout.seasons.data.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ids.TvShowIds
import cinescout.seasons.domain.model.TvShowSeasonsWithEpisodes

interface RemoteSeasonDataSource {

    suspend fun getSeasonsWithEpisodes(tvShowIds: TvShowIds): Either<NetworkError, TvShowSeasonsWithEpisodes>
}
