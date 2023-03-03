package cinescout.database.testdata

import cinescout.database.model.DatabaseMovieKeyword
import cinescout.database.sample.DatabaseMovieSample

object DatabaseMovieKeywordTestData {

    val Corruption = DatabaseMovieKeyword(
        movieId = DatabaseMovieSample.Inception.tmdbId,
        keywordId = DatabaseKeywordTestData.Corruption.tmdbId
    )

    val DrugAddiction = DatabaseMovieKeyword(
        movieId = DatabaseMovieSample.Inception.tmdbId,
        keywordId = DatabaseKeywordTestData.DrugAddiction.tmdbId
    )
}
