package cinescout.tvshows.data.remote.tmdb.mapper

import cinescout.common.model.Keyword
import cinescout.common.model.TmdbKeywordId
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowKeywords
import cinescout.tvshows.domain.model.TvShowKeywords

class TmdbTvShowKeywordMapper {

    fun toTvShowKeywords(keywords: GetTvShowKeywords.Response) = TvShowKeywords(
        tvShowId = keywords.tvShowId,
        keywords = keywords.keywords.map(::toKeyword)
    )

    private fun toKeyword(keyword: GetTvShowKeywords.Response.Keyword) = Keyword(
        id = TmdbKeywordId(keyword.id),
        name = keyword.name
    )
}
