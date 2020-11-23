package profile

import entities.Either
import entities.ResourceError
import entities.model.TmdbProfile
import entities.model.TraktProfile
import entities.profile.ProfileRepository
import kotlinx.coroutines.flow.Flow

internal class ProfileRepositoryImpl(
    private val tmdbProfileRepository: TmdbProfileRepository,
    private val traktProfileRepository: TraktProfileRepository
): ProfileRepository {

    override fun findPersonalTmdbProfile(): Flow<Either<ResourceError, TmdbProfile>> =
        tmdbProfileRepository.findPersonalProfile()

    override fun findPersonalTraktProfile(): Flow<Either<ResourceError, TraktProfile>> =
        traktProfileRepository.findPersonalProfile()
}
