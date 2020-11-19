package profile.tmdb

import entities.Either
import entities.NetworkError
import entities.ResourceError
import entities.flatMapLeft
import entities.model.Profile
import entities.plus
import entities.then
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

    override fun findPersonalProfile(): Flow<Either<ResourceError, Profile>> =
        localSource.findPersonalProfile().flatMapMerge { profileEither ->
            flowOf(profileEither) + interval(RefreshInterval) {
                remoteSource.getPersonalProfile().mapLeft(ResourceError::Network).ifRight {
                    localSource.storePersonalProfile(it)
                }
            }
        }.distinctUntilChanged()

    private companion object {
        val RefreshInterval = 2.minutes
    }
}
