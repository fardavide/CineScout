package cinescout.voting.domain.usecase

import cinescout.screenplay.domain.model.TmdbScreenplayId

interface SetLiked {

    suspend operator fun invoke(screenplayId: TmdbScreenplayId)
}
