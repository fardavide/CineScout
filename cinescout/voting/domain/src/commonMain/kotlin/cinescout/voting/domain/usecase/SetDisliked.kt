package cinescout.voting.domain.usecase

import cinescout.CineScoutTestApi
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.voting.domain.repository.VotedScreenplayRepository
import org.koin.core.annotation.Factory

interface SetDisliked {

    suspend operator fun invoke(screenplayIds: ScreenplayIds)
}

@Factory
internal class RealSetDisliked(
    private val votedScreenplayRepository: VotedScreenplayRepository
) : SetDisliked {

    override suspend operator fun invoke(screenplayIds: ScreenplayIds) =
        votedScreenplayRepository.setDisliked(screenplayIds)
}

@CineScoutTestApi
class FakeSetDisliked : SetDisliked {

    override suspend operator fun invoke(screenplayIds: ScreenplayIds) = Unit
}
