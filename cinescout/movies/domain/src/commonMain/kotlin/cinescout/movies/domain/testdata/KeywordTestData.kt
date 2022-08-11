package cinescout.movies.domain.testdata

import cinescout.movies.domain.model.Keyword
import cinescout.movies.domain.model.TmdbKeywordId

object KeywordTestData {

    val AntiHero = Keyword(
        id = TmdbKeywordId(value = 2095),
        name = "anti-hero"
    )

    val Corruption = Keyword(
        id = TmdbKeywordId(value = 417),
        name = "corruption"
    )

    val DrugAddiction = Keyword(
        id = TmdbKeywordId(value = 1803),
        name = "drug addiction"
    )

    val ParisFrance = Keyword(
        id = TmdbKeywordId(value = 90),
        name = "paris, france"
    )

    val Philosophy = Keyword(
        id = TmdbKeywordId(value = 490),
        name = "philosophy"
    )

    val Spy = Keyword(
        id = TmdbKeywordId(value = 470),
        name = "spy"
    )
}
