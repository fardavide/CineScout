package cinescout.tvshows.data.remote.tmdb.mapper

import cinescout.screenplay.domain.model.Keyword
import cinescout.screenplay.domain.model.TmdbKeywordId
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowKeywords
import cinescout.tvshows.domain.model.TvShowKeywords
import org.koin.core.annotation.Factory

@Factory
internal class TmdbTvShowKeywordMapper {

    fun toTvShowKeywords(keywords: GetTvShowKeywords.Response) = TvShowKeywords(
        tvShowId = keywords.tvShowId,
        keywords = keywords.keywords.map(::toKeyword)
    )

    private fun toKeyword(keyword: GetTvShowKeywords.Response.Keyword) = Keyword(
        id = TmdbKeywordId(keyword.id),
        name = keyword.name
    )
}
