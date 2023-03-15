package cinescout.database.sample

import cinescout.database.model.DatabaseScreenplayCrewMember
import cinescout.database.testdata.DatabasePersonTestData

object DatabaseMovieCrewMemberSample {

    val ChristopherNolan = DatabaseScreenplayCrewMember(
        screenplayId = DatabaseMovieSample.Inception.tmdbId,
        personId = DatabasePersonTestData.ChristopherNolan.tmdbId,
        job = "Director",
        memberOrder = 1
    )
}
