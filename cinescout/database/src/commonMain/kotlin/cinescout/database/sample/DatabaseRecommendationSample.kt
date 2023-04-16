package cinescout.database.sample

import cinescout.database.model.DatabaseRecommendation

object DatabaseRecommendationSample {

    val BreakingBad = DatabaseRecommendation(
        screenplayTraktId = DatabaseTraktScreenplayIdSample.BreakingBad,
        screenplayTmdbId = DatabaseTmdbScreenplayIdSample.BreakingBad
    )
    val Dexter = DatabaseRecommendation(
        screenplayTraktId = DatabaseTraktScreenplayIdSample.Dexter,
        screenplayTmdbId = DatabaseTmdbScreenplayIdSample.Dexter
    )
    val Grimm = DatabaseRecommendation(
        screenplayTraktId = DatabaseTraktScreenplayIdSample.Grimm,
        screenplayTmdbId = DatabaseTmdbScreenplayIdSample.Grimm
    )
    val Inception = DatabaseRecommendation(
        screenplayTraktId = DatabaseTraktScreenplayIdSample.Inception,
        screenplayTmdbId = DatabaseTmdbScreenplayIdSample.Inception
    )
    val TheWolfOfWallStreet = DatabaseRecommendation(
        screenplayTraktId = DatabaseTraktScreenplayIdSample.TheWolfOfWallStreet,
        screenplayTmdbId = DatabaseTmdbScreenplayIdSample.TheWolfOfWallStreet
    )
    val War = DatabaseRecommendation(
        screenplayTraktId = DatabaseTraktScreenplayIdSample.War,
        screenplayTmdbId = DatabaseTmdbScreenplayIdSample.War
    )
}
