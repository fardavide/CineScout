package profile.trakt

import entities.model.TraktProfile
import profile.TraktProfileRepository

interface LocalTraktProfileSource : TraktProfileRepository {

    suspend fun storePersonalProfile(profile: TraktProfile)
}
