package profile.tmdb

import entities.model.TmdbProfile
import profile.TmdbProfileRepository

interface LocalTmdbProfileSource: TmdbProfileRepository {

    suspend fun storePersonalProfile(profile: TmdbProfile)
}
