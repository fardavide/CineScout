package cinescout.suggestions.domain.usecase

import cinescout.screenplay.domain.model.Screenplay
import kotlinx.coroutines.flow.Flow

interface GetAllDislikedScreenplays {

    operator fun invoke(): Flow<List<Screenplay>>
}

class FakeGetAllDislikedScreenplays : GetAllDislikedScreenplays {

    override fun invoke(): Flow<List<Screenplay>> = TODO()
}
