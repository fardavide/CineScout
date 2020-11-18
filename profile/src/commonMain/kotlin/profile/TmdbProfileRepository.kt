package profile

import entities.model.Profile
import kotlinx.coroutines.flow.Flow

interface TmdbProfileRepository {

    fun findPersonalProfile(): Flow<Profile?>
}
