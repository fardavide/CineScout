package cinescout.voting.domain.usecase

import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import kotlinx.coroutines.flow.Flow

interface GetAllDislikedScreenplays {

    operator fun invoke(type: ScreenplayType): Flow<List<Screenplay>>
}
