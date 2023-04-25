package cinescout.screenplay.domain.sample

import cinescout.screenplay.domain.model.MovieKeywords
import cinescout.screenplay.domain.model.TvShowKeywords

object ScreenplayKeywordsSample {

    val Avatar3 = MovieKeywords(
        screenplayId = TmdbScreenplayIdSample.Avatar3,
        keywords = listOf(
            KeywordSample.Avatar,
            KeywordSample.Sequel
        )
    )

    val BreakingBad = TvShowKeywords(
        screenplayId = TmdbScreenplayIdSample.BreakingBad,
        keywords = listOf(
            KeywordSample.DrugDealer,
            KeywordSample.Psychopath,
            KeywordSample.NewMexico
        )
    )

    val Dexter = TvShowKeywords(
        screenplayId = TmdbScreenplayIdSample.Dexter,
        keywords = listOf(
            KeywordSample.DoubleLife,
            KeywordSample.BasedOnNovelOrBook,
            KeywordSample.MiamiFlorida
        )
    )

    val Grimm = TvShowKeywords(
        screenplayId = TmdbScreenplayIdSample.Grimm,
        keywords = listOf(
            KeywordSample.Police,
            KeywordSample.FairyTale,
            KeywordSample.Detective
        )
    )

    val Inception = MovieKeywords(
        screenplayId = TmdbScreenplayIdSample.Inception,
        keywords = listOf(
            KeywordSample.ParisFrance,
            KeywordSample.Spy,
            KeywordSample.Philosophy
        )
    )

    val TheWalkingDeadDeadCity = TvShowKeywords(
        screenplayId = TmdbScreenplayIdSample.TheWalkingDeadDeadCity,
        keywords = listOf(
            KeywordSample.PostApocalypticFuture,
            KeywordSample.BasedOnComic,
            KeywordSample.Survival
        )
    )

    val TheWolfOfWallStreet = MovieKeywords(
        screenplayId = TmdbScreenplayIdSample.TheWolfOfWallStreet,
        keywords = listOf(
            KeywordSample.Corruption,
            KeywordSample.DrugAddiction,
            KeywordSample.AntiHero
        )
    )

    val War = MovieKeywords(
        screenplayId = TmdbScreenplayIdSample.War,
        keywords = listOf(
            KeywordSample.Italy,
            KeywordSample.Portugal,
            KeywordSample.Spy
        )
    )
}
