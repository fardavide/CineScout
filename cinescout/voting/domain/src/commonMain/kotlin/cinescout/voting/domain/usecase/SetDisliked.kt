package cinescout.voting.domain.usecase

import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.voting.domain.repository.VotedScreenplayRepository
import org.koin.core.annotation.Factory

interface SetDisliked {

    suspend operator fun invoke(screenplayId: TmdbScreenplayId)
}

@Factory
internal class RealSetDisliked(
    private val votedScreenplayRepository: VotedScreenplayRepository
) : SetDisliked {

    override suspend operator fun invoke(screenplayId: TmdbScreenplayId) =
        votedScreenplayRepository.setDisliked(screenplayId)
}
