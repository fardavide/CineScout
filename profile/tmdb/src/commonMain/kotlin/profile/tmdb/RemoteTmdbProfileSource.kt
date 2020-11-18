package profile.tmdb

import entities.model.Profile

interface RemoteTmdbProfileSource {

    suspend fun getPersonalProfile(): Profile
}
