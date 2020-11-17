package entities.profile

import entities.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun findPersonalTmdbProfile(): Flow<Profile?>
}
