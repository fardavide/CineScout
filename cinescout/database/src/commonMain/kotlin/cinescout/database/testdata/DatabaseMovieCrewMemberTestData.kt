package cinescout.database.testdata

import cinescout.database.model.DatabaseMovieCrewMember
import cinescout.database.sample.DatabaseMovieSample

object DatabaseMovieCrewMemberTestData {

    val ChristopherNolan = DatabaseMovieCrewMember(
        movieId = DatabaseMovieSample.Inception.tmdbId,
        personId = DatabasePersonTestData.ChristopherNolan.tmdbId,
        job = "Director",
        memberOrder = 1
    )
}
