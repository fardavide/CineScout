package cinescout.database.testdata

import cinescout.database.model.DatabaseMovieCastMember

object DatabaseMovieCastMemberTestData {

    val JosephGordonLevitt = DatabaseMovieCastMember(
        movieId = DatabaseMovieTestData.Inception.tmdbId,
        personId = DatabasePersonTestData.JosephGordonLevitt.tmdbId,
        character = "Dom Cobb"
    )

    val LeonardoDiCaprio = DatabaseMovieCastMember(
        movieId = DatabaseMovieTestData.Inception.tmdbId,
        personId = DatabasePersonTestData.LeonardoDiCaprio.tmdbId,
        character = "Cobb"
    )
}
