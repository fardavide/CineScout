package domain.profile

import entities.Either
import entities.ResourceError
import entities.model.TraktProfile
import entities.profile.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetPersonalTraktProfile(
    private val profile: ProfileRepository
) {

    operator fun invoke(): Flow<Either<ResourceError, TraktProfile>> =
        profile.findPersonalTraktProfile()
}
