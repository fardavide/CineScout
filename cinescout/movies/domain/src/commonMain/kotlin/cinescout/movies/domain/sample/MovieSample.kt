package cinescout.movies.domain.sample

import cinescout.screenplay.domain.sample.ScreenplaySample

@Deprecated(
    "Use ScreenplaySample instead",
    ReplaceWith("ScreenplaySample", "cinescout.screenplay.domain.sample.ScreenplaySample")
)
object MovieSample {

    val Inception = ScreenplaySample.Inception

    val TheWolfOfWallStreet = ScreenplaySample.TheWolfOfWallStreet

    val War = ScreenplaySample.War
}
