package cinescout.screenplay.domain.sample

import cinescout.screenplay.domain.model.TraktScreenplayId

object TraktScreenplayIdSample {

    val BreakingBad = TraktScreenplayId.TvShow(1_388)
    val Dexter = TraktScreenplayId.TvShow(1_396)
    val Grimm = TraktScreenplayId.TvShow(39_185)
    val Inception = TraktScreenplayId.Movie(16_662)
    val TheWolfOfWallStreet = TraktScreenplayId.Movie(75_735)
    val War = TraktScreenplayId.Movie(432_667)
}
