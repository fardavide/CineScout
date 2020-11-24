package profile.trakt

import entities.Either
import entities.ResourceError
import entities.model.TraktProfile
import entities.plus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import profile.TraktProfileRepository
import util.interval
import kotlin.time.minutes

internal class TraktProfileRepositoryImpl(
    private val localSource: LocalTraktProfileSource,
    private val remoteSource: RemoteTraktProfileSource
): TraktProfileRepository {

    override fun findPersonalProfile(): Flow<Either<ResourceError, TraktProfile>> =
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
