package profile.tmdb

import entities.model.Profile

interface RemoteTmdbProfileRepository {

    suspend fun getPersonalProfile(): Profile
}
