package cinescout.database.testdata

import cinescout.database.model.DatabaseMovieKeyword

object DatabaseMovieKeywordTestData {

    val Corruption = DatabaseMovieKeyword(
        movieId = DatabaseMovieTestData.Inception.tmdbId,
        keywordId = DatabaseKeywordTestData.Corruption.tmdbId
    )

    val DrugAddiction = DatabaseMovieKeyword(
        movieId = DatabaseMovieTestData.Inception.tmdbId,
        keywordId = DatabaseKeywordTestData.DrugAddiction.tmdbId
    )
}
