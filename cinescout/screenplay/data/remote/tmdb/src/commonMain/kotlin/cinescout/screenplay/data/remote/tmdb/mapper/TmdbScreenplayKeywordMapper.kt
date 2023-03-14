package cinescout.screenplay.data.remote.tmdb.mapper

import cinescout.screenplay.data.remote.tmdb.model.GetScreenplayKeywordsResponse
import cinescout.screenplay.domain.model.Keyword
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.TmdbKeywordId
import org.koin.core.annotation.Factory

@Factory
internal class TmdbScreenplayKeywordMapper {

    fun toScreenplayKeywords(keywords: GetScreenplayKeywordsResponse) = ScreenplayKeywords(
        keywords = keywords.keywords.map(::toKeyword),
        screenplayId = keywords.screenplayId
    )

    private fun toKeyword(keyword: GetScreenplayKeywordsResponse.Keyword) = Keyword(
        id = TmdbKeywordId(keyword.id),
        name = keyword.name
    )
}
