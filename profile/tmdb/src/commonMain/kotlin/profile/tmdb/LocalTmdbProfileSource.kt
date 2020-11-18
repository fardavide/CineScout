package profile.tmdb

import entities.model.Profile
import profile.TmdbProfileRepository

interface LocalTmdbProfileSource: TmdbProfileRepository {

    suspend fun storePersonalProfile(profile: Profile)
}
