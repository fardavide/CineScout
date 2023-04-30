package cinescout.voting.domain.usecase

import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.voting.domain.repository.VotedScreenplayRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface GetAllLikedScreenplays {

    operator fun invoke(type: ScreenplayTypeFilter): Flow<List<Screenplay>>
}

@Factory
internal class RealGetAllLikedScreenplays(
    private val votedScreenplayRepository: VotedScreenplayRepository
) : GetAllLikedScreenplays {

    override operator fun invoke(type: ScreenplayTypeFilter): Flow<List<Screenplay>> =
        votedScreenplayRepository.getAllLiked(type)
}
