package cinescout.database.testdata

import cinescout.database.model.DatabaseMovieCrewMember

object DatabaseMovieCrewMemberTestData {

    val ChristopherNolan = DatabaseMovieCrewMember(
        movieId = DatabaseMovieTestData.Inception.tmdbId,
        personId = DatabasePersonTestData.ChristopherNolan.tmdbId,
        job = "Director"
    )
}
