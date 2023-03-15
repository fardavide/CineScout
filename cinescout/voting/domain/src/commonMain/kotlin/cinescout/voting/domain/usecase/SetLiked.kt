package cinescout.voting.domain.usecase

import cinescout.screenplay.domain.model.TmdbScreenplayId
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
