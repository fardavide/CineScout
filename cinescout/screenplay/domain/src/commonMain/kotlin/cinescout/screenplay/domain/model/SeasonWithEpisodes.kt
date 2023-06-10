package cinescout.screenplay.domain.model

data class SeasonWithEpisodes(
    val episodes: List<Episode>,
    val season: Season
)
