package profile.tmdb

import entities.model.Profile
import profile.TmdbProfileRepository

interface LocalTmdbProfileRepository: TmdbProfileRepository {

    suspend fun storePersonalProfile(profile: Profile)
}
