package cinescout.movies.data.remote.tmdb.testdata

import cinescout.movies.data.remote.tmdb.model.GetMovieKeywords
import cinescout.movies.domain.sample.MovieCreditsSample
import cinescout.screenplay.domain.sample.KeywordSample

object GetMovieKeywordsResponseTestData {

    val Inception = GetMovieKeywords.Response(
        movieId = MovieCreditsSample.Inception.movieId,
        keywords = listOf(
            GetMovieKeywords.Response.Keyword(
                id = KeywordSample.ParisFrance.id.value,
                name = KeywordSample.ParisFrance.name
            ),
            GetMovieKeywords.Response.Keyword(
                id = KeywordSample.Spy.id.value,
                name = KeywordSample.Spy.name
            ),
            GetMovieKeywords.Response.Keyword(
                id = KeywordSample.Philosophy.id.value,
                name = KeywordSample.Philosophy.name
            )
        )
    )
}
