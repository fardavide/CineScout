package cinescout.people.domain.model

import cinescout.screenplay.domain.model.ids.TmdbMovieId
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TmdbTvShowId

sealed interface ScreenplayCredits {

    val cast: List<CastMember>
    val crew: List<CrewMember>
    val screenplayId: TmdbScreenplayId
}

fun ScreenplayCredits(
    screenplayId: TmdbScreenplayId,
    cast: List<CastMember>,
    crew: List<CrewMember>
): ScreenplayCredits = when (screenplayId) {
    is TmdbMovieId -> MovieCredits(
        screenplayId = screenplayId,
        cast = cast,
        crew = crew
    )
    is TmdbTvShowId -> TvShowCredits(
        screenplayId = screenplayId,
        cast = cast,
        crew = crew
    )
}

data class MovieCredits(
    override val cast: List<CastMember>,
    override val crew: List<CrewMember>,
    override val screenplayId: TmdbMovieId
) : ScreenplayCredits

data class TvShowCredits(
    override val cast: List<CastMember>,
    override val crew: List<CrewMember>,
    override val screenplayId: TmdbTvShowId
) : ScreenplayCredits
