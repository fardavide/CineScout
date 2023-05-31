package cinescout.voting.domain.usecase

import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.voting.domain.repository.VotedScreenplayRepository
import org.koin.core.annotation.Factory

interface SetLiked {

    suspend operator fun invoke(screenplayId: TmdbScreenplayId)
}

@Factory
internal class RealSetLiked(
    private val votedScreenplayRepository: VotedScreenplayRepository
) : SetLiked {

    override suspend operator fun invoke(screenplayId: TmdbScreenplayId) =
        votedScreenplayRepository.setLiked(screenplayId)
}

class FakeSetLiked : SetLiked {

    override suspend operator fun invoke(screenplayId: TmdbScreenplayId) = Unit
}
