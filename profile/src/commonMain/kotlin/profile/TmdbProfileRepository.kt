package profile

import entities.Either
import entities.ResourceError
import entities.model.Profile
import kotlinx.coroutines.flow.Flow

interface TmdbProfileRepository {

    fun findPersonalProfile(): Flow<Either<ResourceError, Profile>>
}
