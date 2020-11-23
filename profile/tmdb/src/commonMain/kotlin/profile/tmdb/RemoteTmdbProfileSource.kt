package profile.tmdb

import entities.Either
import entities.NetworkError
import entities.model.TmdbProfile

interface RemoteTmdbProfileSource {

    suspend fun getPersonalProfile(): Either<NetworkError, TmdbProfile>
}
