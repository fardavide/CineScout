package cinescout.history.domain.usecase

import arrow.core.Either
import arrow.core.right
import cinescout.CineScoutTestApi
import cinescout.error.NetworkError
import cinescout.history.domain.model.HistoryStoreKey
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.history.domain.store.HistoryStore
import cinescout.screenplay.domain.model.SeasonAndEpisodeNumber
import cinescout.screenplay.domain.model.id.EpisodeIds
import cinescout.screenplay.domain.model.id.MovieIds
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.SeasonIds
import cinescout.screenplay.domain.model.id.TvShowIds
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreWriteRequest

interface AddToHistory {

    suspend operator fun invoke(params: Params): Either<NetworkError, Unit> {
        val (key, screenplayIds) = when (params) {
            is Params.Episode -> HistoryStoreKey.Write.Add.Episode(
                episodeIds = params.episodeIds,
                tvShowIds = params.tvShowIds,
                episode = params.episode
            ) to params.tvShowIds
            is Params.Movie -> HistoryStoreKey.Write.Add.Movie(params.movieIds) to params.movieIds
            is Params.Season -> HistoryStoreKey.Write.Add.Season(
                seasonIds = params.seasonIds,
                tvShowIds = params.tvShowIds,
                episodes = params.episodes
            ) to params.tvShowIds
            is Params.TvShow -> HistoryStoreKey.Write.Add.TvShow(
                tvShowIds = params.tvShowIds,
                episodes = params.episodes
            ) to params.tvShowIds
        }
        return invoke(screenplayIds = screenplayIds, key = key)
    }

    suspend operator fun invoke(
        screenplayIds: ScreenplayIds,
        key: HistoryStoreKey.Write.Add
    ): Either<NetworkError, Unit>

    sealed interface Params {

        data class Episode(
            val tvShowIds: TvShowIds,
            val episodeIds: EpisodeIds,
            val episode: SeasonAndEpisodeNumber
        ) : Params

        data class Movie(val movieIds: MovieIds) : Params

        data class Season(
            val tvShowIds: TvShowIds,
            val seasonIds: SeasonIds,
            val episodes: List<SeasonAndEpisodeNumber>
        ) : Params

        data class TvShow(
            val tvShowIds: TvShowIds,
            val episodes: List<SeasonAndEpisodeNumber>
        ) : Params
    }
}

@Factory
internal class RealAddToHistory(
    private val store: HistoryStore
) : AddToHistory {

    override suspend fun invoke(
        screenplayIds: ScreenplayIds,
        key: HistoryStoreKey.Write.Add
    ): Either<NetworkError, Unit> = store.write(
        StoreWriteRequest.of(
            key = key,
            value = ScreenplayHistory.empty(screenplayIds)
        )
    )
}

@CineScoutTestApi
class FakeAddToHistory : AddToHistory {

    override suspend fun invoke(
        screenplayIds: ScreenplayIds,
        key: HistoryStoreKey.Write.Add
    ): Either<NetworkError, Unit> = Unit.right()
}
