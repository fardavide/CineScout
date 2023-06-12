package cinescout.history.domain.usecase

import arrow.core.Either
import arrow.core.right
import cinescout.CineScoutTestApi
import cinescout.error.NetworkError
import cinescout.history.domain.model.HistoryStoreKey
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.history.domain.store.HistoryStore
import cinescout.screenplay.domain.model.SeasonAndEpisodeNumber
import cinescout.screenplay.domain.model.ids.EpisodeIds
import cinescout.screenplay.domain.model.ids.MovieIds
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.SeasonIds
import cinescout.screenplay.domain.model.ids.TvShowIds
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreWriteRequest

interface AddToHistory {

    suspend operator fun invoke(movieIds: MovieIds): Either<NetworkError, Unit> = invoke(
        screenplayIds = movieIds,
        key = HistoryStoreKey.Write.Add.Movie(movieIds)
    )

    suspend operator fun invoke(
        tvShowIds: TvShowIds,
        episodeIds: EpisodeIds,
        episode: SeasonAndEpisodeNumber
    ): Either<NetworkError, Unit> = invoke(
        screenplayIds = tvShowIds,
        key = HistoryStoreKey.Write.Add.Episode(episodeIds, tvShowIds, episode)
    )

    suspend operator fun invoke(
        tvShowIds: TvShowIds,
        seasonIds: SeasonIds,
        episodes: List<SeasonAndEpisodeNumber>
    ): Either<NetworkError, Unit> = invoke(
        screenplayIds = tvShowIds,
        key = HistoryStoreKey.Write.Add.Seasons(seasonIds, tvShowIds, episodes)
    )

    suspend operator fun invoke(
        tvShowIds: TvShowIds,
        episodes: List<SeasonAndEpisodeNumber>
    ): Either<NetworkError, Unit> = invoke(
        screenplayIds = tvShowIds,
        key = HistoryStoreKey.Write.Add.TvShow(tvShowIds, episodes)
    )

    suspend operator fun invoke(
        screenplayIds: ScreenplayIds,
        key: HistoryStoreKey.Write.Add
    ): Either<NetworkError, Unit>
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
