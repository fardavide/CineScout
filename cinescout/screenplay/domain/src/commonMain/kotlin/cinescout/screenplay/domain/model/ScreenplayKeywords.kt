package cinescout.screenplay.domain.model

import cinescout.screenplay.domain.model.id.TmdbMovieId
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import cinescout.screenplay.domain.model.id.TmdbTvShowId

sealed interface ScreenplayKeywords {

    val keywords: List<Keyword>
    val screenplayId: TmdbScreenplayId
}

fun ScreenplayKeywords(keywords: List<Keyword>, screenplayId: TmdbScreenplayId): ScreenplayKeywords =
    when (screenplayId) {
        is TmdbMovieId -> MovieKeywords(keywords, screenplayId)
        is TmdbTvShowId -> TvShowKeywords(keywords, screenplayId)
    }

data class MovieKeywords(
    override val keywords: List<Keyword>,
    override val screenplayId: TmdbMovieId
) : ScreenplayKeywords

data class TvShowKeywords(
    override val keywords: List<Keyword>,
    override val screenplayId: TmdbTvShowId
) : ScreenplayKeywords
