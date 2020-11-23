package profile

import entities.Either
import entities.ResourceError
import entities.model.TmdbProfile
import kotlinx.coroutines.flow.Flow

interface TmdbProfileRepository {

    fun findPersonalProfile(): Flow<Either<ResourceError, TmdbProfile>>
}
