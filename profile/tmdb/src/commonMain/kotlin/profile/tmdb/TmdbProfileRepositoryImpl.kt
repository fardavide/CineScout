package profile.tmdb

import entities.model.Profile
import entities.plus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import profile.TmdbProfileRepository
import util.interval
import kotlin.time.minutes

internal class TmdbProfileRepositoryImpl(
    private val localRepository: LocalTmdbProfileRepository,
    private val remoteRepository: RemoteTmdbProfileRepository
): TmdbProfileRepository {

    override fun findPersonalProfile(): Flow<Profile?> =
        localRepository.findPersonalProfile().flatMapMerge {
            flowOf(it) + interval(RefreshInterval) {
                remoteRepository.getPersonalProfile().also {
                    localRepository.storePersonalProfile(remoteRepository.getPersonalProfile())
                }
            }
        }

    private companion object {
        val RefreshInterval = 2.minutes
    }
}
