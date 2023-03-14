package cinescout.people.domain.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

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
    is TmdbScreenplayId.Movie -> MovieCredits(
        screenplayId = screenplayId,
        cast = cast,
        crew = crew
    )
    is TmdbScreenplayId.TvShow -> TvShowCredits(
        screenplayId = screenplayId,
        cast = cast,
        crew = crew
    )
}

data class MovieCredits(
    override val cast: List<CastMember>,
    override val crew: List<CrewMember>,
    override val screenplayId: TmdbScreenplayId.Movie
) : ScreenplayCredits

data class TvShowCredits(
    override val cast: List<CastMember>,
    override val crew: List<CrewMember>,
    override val screenplayId: TmdbScreenplayId.TvShow
) : ScreenplayCredits
