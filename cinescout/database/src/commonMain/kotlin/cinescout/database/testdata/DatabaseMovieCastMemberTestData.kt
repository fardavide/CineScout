package cinescout.database.testdata

import cinescout.database.model.DatabaseMovieCastMember
import cinescout.database.sample.DatabaseMovieSample

object DatabaseMovieCastMemberTestData {

    val JosephGordonLevitt = DatabaseMovieCastMember(
        movieId = DatabaseMovieSample.Inception.tmdbId,
        personId = DatabasePersonTestData.JosephGordonLevitt.tmdbId,
        character = "Dom Cobb",
        memberOrder = 2
    )

    val LeonardoDiCaprio = DatabaseMovieCastMember(
        movieId = DatabaseMovieSample.Inception.tmdbId,
        personId = DatabasePersonTestData.LeonardoDiCaprio.tmdbId,
        character = "Cobb",
        memberOrder = 1
    )
}
