package cinescout.voting.domain.usecase

import cinescout.screenplay.domain.model.TmdbScreenplayId

interface SetDisliked {

    suspend operator fun invoke(screenplayId: TmdbScreenplayId)
}
