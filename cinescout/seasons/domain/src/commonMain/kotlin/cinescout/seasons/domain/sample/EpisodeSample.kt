package cinescout.seasons.domain.sample

import arrow.core.some
import cinescout.screenplay.domain.model.EpisodeNumber
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.SeasonNumber
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.model.ids.EpisodeIds
import cinescout.screenplay.domain.model.ids.TmdbEpisodeId
import cinescout.screenplay.domain.model.ids.TraktEpisodeId
import cinescout.seasons.domain.model.Episode
import korlibs.time.Date
import kotlin.time.Duration.Companion.minutes

object EpisodeSample {

    val BreakingBad_s0e1 = Episode(
        firstAirDate = Date(year = 2009, month = 2, day = 18).some(),
        ids = EpisodeIds(
            tmdb = TmdbEpisodeId(62_131),
            trakt = TraktEpisodeId(73_476)
        ),
        number = EpisodeNumber(1),
        overview = "Season 1 Minisode 1: Hank and Marie celebrate Valentine's Day.",
        rating = PublicRating(
            average = Rating.of(8.58108).getOrThrow(),
            voteCount = 370
        ),
        runtime = 3.minutes,
        seasonNumber = SeasonNumber(0),
        title = "Good Cop / Bad Cop"
    )

    val BreakingBad_s0e2 = Episode(
        firstAirDate = Date(year = 2009, month = 2, day = 18).some(),
        ids = EpisodeIds(
            tmdb = TmdbEpisodeId(62_133),
            trakt = TraktEpisodeId(73_477)
        ),
        number = EpisodeNumber(2),
        overview = "Season 1 Minisode 2: Hank and Walt have a discussion on Hank and Marie's wedding day.",
        rating = PublicRating(
            average = Rating.of(8.3).getOrThrow(),
            voteCount = 150
        ),
        runtime = 5.minutes,
        seasonNumber = SeasonNumber(0),
        title = "Wedding Day"
    )

    val BreakingBad_s0e3 = Episode(
        firstAirDate = Date(year = 2009, month = 2, day = 18).some(),
        ids = EpisodeIds(
            tmdb = TmdbEpisodeId(62132),
            trakt = TraktEpisodeId(73478)
        ),
        number = EpisodeNumber(3),
        overview = "Season 1 Minisode 3: Jesse explains the origins of his band, TwaüghtHammër.",
        rating = PublicRating(
            average = Rating.of(8.18182).getOrThrow(),
            voteCount = 121
        ),
        runtime = 5.minutes,
        seasonNumber = SeasonNumber(0),
        title = "TwaüghtHammër"
    )

    val BreakingBad_s1e1 = Episode(
        firstAirDate = Date(year = 2008, month = 1, day = 21).some(),
        ids = EpisodeIds(
            tmdb = TmdbEpisodeId(62085),
            trakt = TraktEpisodeId(73482)
        ),
        number = EpisodeNumber(1),
        overview = "When an unassuming high school chemistry teacher discovers he has a rare form of lung cancer, " +
            "he decides to team up with a former student and create a top of the line crystal meth in a used RV, to " +
            "provide for his family once he is gone.",
        rating = PublicRating(
            average = Rating.of(8.35528).getOrThrow(),
            voteCount = 7487
        ),
        runtime = 59.minutes,
        seasonNumber = SeasonNumber(1),
        title = "Pilot"
    )

    val BreakingBad_s1e2 = Episode(
        firstAirDate = Date(year = 2008, month = 1, day = 28).some(),
        ids = EpisodeIds(
            tmdb = TmdbEpisodeId(62086),
            trakt = TraktEpisodeId(73483)
        ),
        number = EpisodeNumber(2),
        overview = "Walt and Jesse attempt to tie up loose ends. The desperate situation gets more complicated " +
            "with the flip of a coin. Walt's wife, Skyler, becomes suspicious of Walt's strange behavior.",
        rating = PublicRating(
            average = Rating.of(8.12349).getOrThrow(),
            voteCount = 5968
        ),
        runtime = 49.minutes,
        seasonNumber = SeasonNumber(1),
        title = "Cat's in the Bag..."
    )

    val BreakingBad_s1e3 = Episode(
        firstAirDate = Date(year = 2008, month = 2, day = 11).some(),
        ids = EpisodeIds(
            tmdb = TmdbEpisodeId(62087),
            trakt = TraktEpisodeId(73484)
        ),
        number = EpisodeNumber(3),
        overview = "Walter fights with Jesse over his drug use, causing him to leave Walter alone with their " +
            "captive, Krazy-8. Meanwhile, Hank has a scared straight moment with Walter Jr. after his aunt " +
            "discovers he has been smoking pot. Also, Skylar is upset when Walter stays away from home.",
        rating = PublicRating(
            average = Rating.of(8.06034).getOrThrow(),
            voteCount = 5436
        ),
        runtime = 48.minutes,
        seasonNumber = SeasonNumber(1),
        title = "...And the Bag's in the River"
    )

    val BreakingBad_s2e1 = Episode(
        firstAirDate = Date(year = 2009, month = 3, day = 9).some(),
        ids = EpisodeIds(
            tmdb = TmdbEpisodeId(972_873),
            trakt = TraktEpisodeId(73_489)
        ),
        number = EpisodeNumber(1),
        overview = "Walt and Jesse are vividly reminded of Tuco’s volatile nature, and try to figure a way out " +
            "of their business partnership. Hank attempts to mend fences between the estranged Marie and Skyler.",
        rating = PublicRating(
            average = Rating.of(8.15105).getOrThrow(),
            voteCount = 4932
        ),
        runtime = 48.minutes,
        seasonNumber = SeasonNumber(2),
        title = "Seven Thirty-Seven"
    )

    val BreakingBad_s2e2 = Episode(
        firstAirDate = Date(year = 2009, month = 3, day = 16).some(),
        ids = EpisodeIds(
            tmdb = TmdbEpisodeId(972_874),
            trakt = TraktEpisodeId(73_490)
        ),
        number = EpisodeNumber(2),
        overview = "Walt and Jesse find themselves in close quarters with an unhinged Tuco. Marie and Hank comfort " +
            "Skyler, who is distraught over Walt’s disappearance. Hank pays a visit to Mrs. Pinkman on some " +
            "not-so-official business.",
        rating = PublicRating(
            average = Rating.of(8.35487).getOrThrow(),
            voteCount = 4892
        ),
        runtime = 48.minutes,
        seasonNumber = SeasonNumber(2),
        title = "Grilled"
    )

    val BreakingBad_s2e3 = Episode(
        firstAirDate = Date(year = 2009, month = 3, day = 23).some(),
        ids = EpisodeIds(
            tmdb = TmdbEpisodeId(62_094),
            trakt = TraktEpisodeId(73_491)
        ),
        number = EpisodeNumber(3),
        overview = "Walt and Jesse become short on cash when they try to cover their tracks. Meanwhile, the DEA " +
            "has a lead that could them straight to Walt and Jesse.",
        rating = PublicRating(
            average = Rating.of(7.96222).getOrThrow(),
            voteCount = 4632
        ),
        runtime = 47.minutes,
        seasonNumber = SeasonNumber(2),
        title = "Bit by a Dead Bee"
    )
}
