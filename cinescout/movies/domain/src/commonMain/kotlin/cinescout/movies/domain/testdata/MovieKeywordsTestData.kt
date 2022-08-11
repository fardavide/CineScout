package cinescout.movies.domain.testdata

import cinescout.movies.domain.model.MovieKeywords

object MovieKeywordsTestData {

    val Inception = MovieKeywords(
        movieId = TmdbMovieIdTestData.Inception,
        keywords = listOf(
            KeywordTestData.ParisFrance,
            KeywordTestData.Spy,
            KeywordTestData.Philosophy
        )
    )

    val TheWolfOfWallStreet = MovieKeywords(
        movieId = TmdbMovieIdTestData.TheWolfOfWallStreet,
        keywords = listOf(
            KeywordTestData.Corruption,
            KeywordTestData.DrugAddiction,
            KeywordTestData.AntiHero
        )
    )

    val War = MovieKeywords(
        movieId = TmdbMovieIdTestData.War,
        keywords = emptyList()
    )
}
