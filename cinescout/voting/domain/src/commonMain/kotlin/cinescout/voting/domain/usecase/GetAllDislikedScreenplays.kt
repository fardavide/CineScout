package cinescout.voting.domain.usecase

import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.voting.domain.repository.VotedScreenplayRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface GetAllDislikedScreenplays {

    operator fun invoke(type: ScreenplayTypeFilter): Flow<List<Screenplay>>
}

@Factory
internal class RealGetAllDislikedScreenplays(
    private val votedScreenplayRepository: VotedScreenplayRepository
) : GetAllDislikedScreenplays {

    override operator fun invoke(type: ScreenplayTypeFilter): Flow<List<Screenplay>> =
        votedScreenplayRepository.getAllDisliked(type)
}
