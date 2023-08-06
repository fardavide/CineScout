package cinescout.seasons.data.store

import cinescout.screenplay.domain.model.id.TvShowIds
import cinescout.seasons.data.datasource.LocalSeasonDataSource
import cinescout.seasons.data.datasource.RemoteSeasonDataSource
import cinescout.seasons.domain.model.TvShowSeasonsWithEpisodes
import cinescout.seasons.domain.store.TvShowSeasonsWithEpisodesStore
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [TvShowSeasonsWithEpisodesStore::class])
internal class RealTvShowSeasonsWithEpisodesStore(
    private val localDataSource: LocalSeasonDataSource,
    private val remoteDataSource: RemoteSeasonDataSource
) : TvShowSeasonsWithEpisodesStore,
    Store5<TvShowIds, TvShowSeasonsWithEpisodes> by Store5Builder
        .from(
            fetcher = EitherFetcher.of(remoteDataSource::getSeasonsWithEpisodes),
            sourceOfTruth = SourceOfTruth.of(
                reader = localDataSource::findSeasonsWithEpisodes,
                writer = { _, seasonsWithEpisodes -> localDataSource.insertSeasonsWithEpisodes(seasonsWithEpisodes) }
            )
        )
        .build()
