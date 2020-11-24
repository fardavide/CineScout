package profile

import entities.Either
import entities.ResourceError
import entities.model.TraktProfile
import kotlinx.coroutines.flow.Flow

interface TraktProfileRepository {

    fun findPersonalProfile(): Flow<Either<ResourceError, TraktProfile>>
}
