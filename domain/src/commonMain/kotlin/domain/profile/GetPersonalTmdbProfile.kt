package domain.profile

import entities.Either
import entities.ResourceError
import entities.model.TmdbProfile
import entities.profile.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetPersonalTmdbProfile(
    private val profile: ProfileRepository
) {

    operator fun invoke(): Flow<Either<ResourceError, TmdbProfile>> =
        profile.findPersonalTmdbProfile()
}
