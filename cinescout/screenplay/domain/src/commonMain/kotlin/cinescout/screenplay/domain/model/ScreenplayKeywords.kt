package cinescout.screenplay.domain.model

sealed interface ScreenplayKeywords {

    val keywords: List<Keyword>
    val screenplayId: TmdbScreenplayId
}

fun ScreenplayKeywords(keywords: List<Keyword>, screenplayId: TmdbScreenplayId): ScreenplayKeywords =
    when (screenplayId) {
        is TmdbScreenplayId.Movie -> MovieKeywords(keywords, screenplayId)
        is TmdbScreenplayId.TvShow -> TvShowKeywords(keywords, screenplayId)
    }

data class MovieKeywords(
    override val keywords: List<Keyword>,
    override val screenplayId: TmdbScreenplayId.Movie
) : ScreenplayKeywords

data class TvShowKeywords(
    override val keywords: List<Keyword>,
    override val screenplayId: TmdbScreenplayId.TvShow
) : ScreenplayKeywords
