package cinescout.rating.domain.sample

import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow

object ScreenplayPersonalRatingSample {

    val BreakingBad = Rating.of(7).getOrThrow()

    val Dexter = Rating.of(8).getOrThrow()

    val Grimm = Rating.of(9).getOrThrow()

    val Inception = Rating.of(9).getOrThrow()

    val TheWolfOfWallStreet = Rating.of(8).getOrThrow()

    val War = Rating.of(6).getOrThrow()
}
