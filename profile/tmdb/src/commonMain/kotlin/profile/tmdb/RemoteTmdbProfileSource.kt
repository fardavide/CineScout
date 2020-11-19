package profile.tmdb

import entities.Either
import entities.NetworkError
import entities.model.Profile

interface RemoteTmdbProfileSource {

    suspend fun getPersonalProfile(): Either<NetworkError, Profile>
}
