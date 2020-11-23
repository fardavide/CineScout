package profile.trakt

import entities.Either
import entities.NetworkError
import entities.model.TraktProfile

interface RemoteTraktProfileSource {

    suspend fun getPersonalProfile(): Either<NetworkError, TraktProfile>
}
