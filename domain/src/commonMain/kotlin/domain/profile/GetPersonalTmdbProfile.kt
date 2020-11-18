package domain.profile

import entities.model.Profile
import entities.profile.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetPersonalTmdbProfile(
    private val profile: ProfileRepository
) {

    operator fun invoke(): Flow<Profile?> =
        profile.findPersonalTmdbProfile()
}
