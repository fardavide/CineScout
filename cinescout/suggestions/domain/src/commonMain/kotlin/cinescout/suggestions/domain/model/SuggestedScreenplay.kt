package cinescout.suggestions.domain.model

import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TvShow

sealed interface SuggestedScreenplay {

    val affinity: Affinity
    val screenplay: Screenplay
    val source: SuggestionSource
}

fun SuggestedScreenplay(screenplay: Screenplay, source: SuggestionSource) = when (screenplay) {
    is Movie -> SuggestedMovie(screenplay, source)
    is TvShow -> SuggestedTvShow(screenplay, source)
}

data class SuggestedMovie(
    override val affinity: Affinity,
    override val screenplay: Movie,
    override val source: SuggestionSource
) : SuggestedScreenplay

fun SuggestedMovie(movie: Movie, source: SuggestionSource) = SuggestedMovie(
    affinity = Affinity.from(source),
    screenplay = movie,
    source = source
)

data class SuggestedTvShow(
    override val affinity: Affinity,
    override val screenplay: TvShow,
    override val source: SuggestionSource
) : SuggestedScreenplay

fun SuggestedTvShow(tvShow: TvShow, source: SuggestionSource) = SuggestedTvShow(
    affinity = Affinity.from(source),
    screenplay = tvShow,
    source = source
)

fun Collection<SuggestedScreenplay>.suggestionIds(): List<SuggestedScreenplayId> = map {
    SuggestedScreenplayId(
        screenplayIds = it.screenplay.ids,
        source = it.source
    )
}
