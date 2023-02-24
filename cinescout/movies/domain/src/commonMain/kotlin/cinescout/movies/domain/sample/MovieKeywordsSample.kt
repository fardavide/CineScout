package cinescout.movies.domain.sample

import cinescout.common.testdata.KeywordTestData
import cinescout.movies.domain.model.MovieKeywords

object MovieKeywordsSample {

    val Inception = MovieKeywords(
        movieId = TmdbMovieIdSample.Inception,
        keywords = listOf(
            KeywordTestData.ParisFrance,
            KeywordTestData.Spy,
            KeywordTestData.Philosophy
        )
    )

    val TheWolfOfWallStreet = MovieKeywords(
        movieId = TmdbMovieIdSample.TheWolfOfWallStreet,
        keywords = listOf(
            KeywordTestData.Corruption,
            KeywordTestData.DrugAddiction,
            KeywordTestData.AntiHero
        )
    )

    val War = MovieKeywords(
        movieId = TmdbMovieIdSample.War,
        keywords = listOf(
            KeywordTestData.Italy,
            KeywordTestData.Portugal,
            KeywordTestData.Spy
        )
    )
}