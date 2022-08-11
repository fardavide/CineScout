package cinescout.movies.data.remote.tmdb.testdata

import cinescout.movies.data.remote.tmdb.model.GetMovieKeywords
import cinescout.movies.domain.testdata.KeywordTestData
import cinescout.movies.domain.testdata.MovieCreditsTestData

object GetMovieKeywordsResponseTestData {

    val Inception = GetMovieKeywords.Response(
        movieId = MovieCreditsTestData.Inception.movieId,
        keywords = listOf(
            GetMovieKeywords.Response.Keyword(
                id = KeywordTestData.ParisFrance.id.value,
                name = KeywordTestData.ParisFrance.name
            ),
            GetMovieKeywords.Response.Keyword(
                id = KeywordTestData.Spy.id.value,
                name = KeywordTestData.Spy.name
            ),
            GetMovieKeywords.Response.Keyword(
                id = KeywordTestData.Philosophy.id.value,
                name = KeywordTestData.Philosophy.name
            )
        )
    )
}
