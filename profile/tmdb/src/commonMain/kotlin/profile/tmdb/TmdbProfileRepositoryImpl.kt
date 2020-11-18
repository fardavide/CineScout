package profile.tmdb

import entities.model.Profile
import entities.plus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import profile.TmdbProfileRepository
import util.interval
import kotlin.time.minutes

internal class TmdbProfileRepositoryImpl(
    private val localSource: LocalTmdbProfileSource,
    private val remoteSource: RemoteTmdbProfileSource
): TmdbProfileRepository {

    override fun findPersonalProfile(): Flow<Profile?> =
        localSource.findPersonalProfile().flatMapMerge { profile ->
            flowOf(profile) + interval(RefreshInterval) {
                remoteSource.getPersonalProfile().also {
                    localSource.storePersonalProfile(it)
                }
            }
        }.distinctUntilChanged()

    private companion object {
        val RefreshInterval = 2.minutes
    }
}
