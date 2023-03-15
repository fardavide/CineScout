package cinescout.database.sample

import cinescout.database.model.DatabaseScreenplayKeyword
import cinescout.database.testdata.DatabaseKeywordTestData

object DatabaseScreenplayKeywordSample {

    val Corruption = DatabaseScreenplayKeyword(
        screenplayId = DatabaseMovieSample.Inception.tmdbId,
        keywordId = DatabaseKeywordTestData.Corruption.tmdbId
    )

    val DrugAddiction = DatabaseScreenplayKeyword(
        screenplayId = DatabaseMovieSample.Inception.tmdbId,
        keywordId = DatabaseKeywordTestData.DrugAddiction.tmdbId
    )
}
