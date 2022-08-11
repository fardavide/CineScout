package cinescout.database.testdata

import cinescout.database.model.DatabaseKeyword
import cinescout.database.model.DatabaseTmdbKeywordId

object DatabaseKeywordTestData {

    val Corruption = DatabaseKeyword(
        tmdbId = DatabaseTmdbKeywordId(value = 417),
        name = "corruption"
    )

    val DrugAddiction = DatabaseKeyword(
        tmdbId = DatabaseTmdbKeywordId(value = 1803),
        name = "Drug Addiction"
    )
}
