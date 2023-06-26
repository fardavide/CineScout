package cinescout.voting.domain.usecase

import cinescout.CineScoutTestApi
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.voting.domain.repository.VotedScreenplayRepository
import org.koin.core.annotation.Factory

interface SetLiked {

    suspend operator fun invoke(screenplayIds: ScreenplayIds)
}

@Factory
internal class RealSetLiked(
    private val votedScreenplayRepository: VotedScreenplayRepository
) : SetLiked {

    override suspend operator fun invoke(screenplayIds: ScreenplayIds) =
        votedScreenplayRepository.setLiked(screenplayIds)
}

@CineScoutTestApi
class FakeSetLiked : SetLiked {

    override suspend operator fun invoke(screenplayIds: ScreenplayIds) = Unit
}
