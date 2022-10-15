package cinescout.common.testdata

import cinescout.common.model.Keyword
import cinescout.common.model.TmdbKeywordId

object KeywordTestData {

    val AntiHero = Keyword(
        id = TmdbKeywordId(value = 2095),
        name = "anti-hero"
    )

    val Corruption = Keyword(
        id = TmdbKeywordId(value = 417),
        name = "corruption"
    )

    val Detective = Keyword(
        id = TmdbKeywordId(value = 703),
        name = "detective"
    )

    val DrugAddiction = Keyword(
        id = TmdbKeywordId(value = 1803),
        name = "drug addiction"
    )

    val FairyTale = Keyword(
        id = TmdbKeywordId(value = 3_205),
        name = "fairy tale"
    )

    val ParisFrance = Keyword(
        id = TmdbKeywordId(value = 90),
        name = "paris, france"
    )

    val Police = Keyword(
        id = TmdbKeywordId(value = 6_149),
        name = "police"
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
