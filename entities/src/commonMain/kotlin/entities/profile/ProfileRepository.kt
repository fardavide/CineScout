package entities.profile

import entities.Either
import entities.ResourceError
import entities.model.TmdbProfile
import entities.model.TraktProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun findPersonalTmdbProfile(): Flow<Either<ResourceError, TmdbProfile>>
    fun findPersonalTraktProfile(): Flow<Either<ResourceError, TraktProfile>>
}
