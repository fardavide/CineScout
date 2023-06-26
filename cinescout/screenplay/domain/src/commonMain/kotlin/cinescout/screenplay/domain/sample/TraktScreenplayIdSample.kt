package cinescout.screenplay.domain.sample

import cinescout.screenplay.domain.model.id.TraktEpisodeId
import cinescout.screenplay.domain.model.id.TraktMovieId
import cinescout.screenplay.domain.model.id.TraktTvShowId

object TraktScreenplayIdSample {

    val Avatar3 = TraktMovieId(62_544)
    val BreakingBad = TraktTvShowId(1_388)
    val BreakingBad_s1e1 = TraktEpisodeId(73_482)
    val BreakingBad_s1e2 = TraktEpisodeId(73_483)
    val BreakingBad_s1e3 = TraktEpisodeId(73_484)
    val Dexter = TraktTvShowId(1_396)
    val Grimm = TraktTvShowId(39_185)
    val Inception = TraktMovieId(16_662)
    val Sherlock = TraktTvShowId(19_792)
    val TheWalkingDeadDeadCity = TraktTvShowId(193_872)
    val TheWolfOfWallStreet = TraktMovieId(75_735)
    val War = TraktMovieId(432_667)
}
