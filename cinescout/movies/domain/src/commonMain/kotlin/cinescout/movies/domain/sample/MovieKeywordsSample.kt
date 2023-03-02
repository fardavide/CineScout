package cinescout.movies.domain.sample

import cinescout.movies.domain.model.MovieKeywords
import cinescout.screenplay.domain.sample.KeywordSample

object MovieKeywordsSample {

    val Inception = MovieKeywords(
        movieId = TmdbMovieIdSample.Inception,
        keywords = listOf(
            KeywordSample.ParisFrance,
            KeywordSample.Spy,
            KeywordSample.Philosophy
        )
    )

    val TheWolfOfWallStreet = MovieKeywords(
        movieId = TmdbMovieIdSample.TheWolfOfWallStreet,
        keywords = listOf(
            KeywordSample.Corruption,
            KeywordSample.DrugAddiction,
            KeywordSample.AntiHero
        )
    )

    val War = MovieKeywords(
        movieId = TmdbMovieIdSample.War,
        keywords = listOf(
            KeywordSample.Italy,
            KeywordSample.Portugal,
            KeywordSample.Spy
        )
    )
}
