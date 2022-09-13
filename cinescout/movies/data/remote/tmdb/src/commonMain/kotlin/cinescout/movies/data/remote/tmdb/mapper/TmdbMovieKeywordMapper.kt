package cinescout.movies.data.remote.tmdb.mapper

import cinescout.movies.data.remote.tmdb.model.GetMovieKeywords
import cinescout.movies.domain.model.Keyword
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.TmdbKeywordId

class TmdbMovieKeywordMapper {

    fun toMovieKeywords(keywords: GetMovieKeywords.Response) = MovieKeywords(
        movieId = keywords.movieId,
        keywords = keywords.keywords.map(::toKeyword)
    )

    private fun toKeyword(keyword: GetMovieKeywords.Response.Keyword) = Keyword(
        id = TmdbKeywordId(keyword.id),
        name = keyword.name
    )
}
