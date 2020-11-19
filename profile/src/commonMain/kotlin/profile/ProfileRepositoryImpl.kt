package profile

import entities.Either
import entities.ResourceError
import entities.model.Profile
import entities.profile.ProfileRepository
import kotlinx.coroutines.flow.Flow

internal class ProfileRepositoryImpl(
    private val tmdbProfileRepository: TmdbProfileRepository
): ProfileRepository {

    override fun findPersonalTmdbProfile(): Flow<Either<ResourceError, Profile>> =
        tmdbProfileRepository.findPersonalProfile()
}
