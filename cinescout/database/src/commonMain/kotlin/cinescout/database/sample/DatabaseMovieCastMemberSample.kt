package cinescout.database.sample

import cinescout.database.model.DatabaseScreenplayCastMember
import cinescout.database.testdata.DatabasePersonTestData

object DatabaseMovieCastMemberSample {

    val JosephGordonLevitt = DatabaseScreenplayCastMember(
        screenplayId = DatabaseMovieSample.Inception.tmdbId,
        personId = DatabasePersonTestData.JosephGordonLevitt.tmdbId,
        character = "Dom Cobb",
        memberOrder = 2
    )

    val LeonardoDiCaprio = DatabaseScreenplayCastMember(
        screenplayId = DatabaseMovieSample.Inception.tmdbId,
        personId = DatabasePersonTestData.LeonardoDiCaprio.tmdbId,
        character = "Cobb",
        memberOrder = 1
    )
}
