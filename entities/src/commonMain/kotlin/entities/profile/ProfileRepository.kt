package entities.profile

import entities.Either
import entities.ResourceError
import entities.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun findPersonalTmdbProfile(): Flow<Either<ResourceError, Profile>>
}
