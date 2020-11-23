package profile.trakt

import entities.Either
import entities.NetworkError
import entities.model.TmdbProfile

interface RemoteTraktProfileSource {

    suspend fun getPersonalProfile(): Either<NetworkError, TmdbProfile>
}
