package cinescout.suggestions.domain.usecase

import cinescout.screenplay.domain.model.Screenplay
import kotlinx.coroutines.flow.Flow

interface GetAllLikedScreenplays {

    operator fun invoke(): Flow<List<Screenplay>>
}

class FakeGetAllLikedScreenplays : GetAllLikedScreenplays {

    override fun invoke(): Flow<List<Screenplay>> = TODO()
}
