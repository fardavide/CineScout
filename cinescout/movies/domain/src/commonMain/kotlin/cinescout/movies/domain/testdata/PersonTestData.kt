package cinescout.movies.domain.testdata

import arrow.core.some
import cinescout.movies.domain.model.Person
import cinescout.movies.domain.model.TmdbPersonId
import cinescout.movies.domain.model.TmdbProfileImage

object PersonTestData {

    val ChristopherNolan = Person(
        name = "Christopher Nolan",
        profileImage = TmdbProfileImage(path = "/xuAIuYSmsUzKlUMBFGVZaWsY3DZ.jpg").some(),
        tmdbId = TmdbPersonId(value = 525)
    )

    val CillianMurphy = Person(
        name = "Cillian Murphy",
        profileImage = TmdbProfileImage(path = "/i8dOTC0w6V274ev5iAAvo4Ahhpr.jpg").some(),
        tmdbId = TmdbPersonId(value = 2_037)
    )

    val DileepRao = Person(
        name = "Dileep Rao",
        profileImage = TmdbProfileImage(path = "/k4OiYIhr886ollgOrvrBs2ZIZbc.jpg").some(),
        tmdbId = TmdbPersonId(value = 95_697)
    )

    val ElliotPage = Person(
        name = "Elliot Page",
        profileImage = TmdbProfileImage(path = "/eCeFgzS8dYHnMfWQT0oQitCrsSz.jpg").some(),
        tmdbId = TmdbPersonId(value = 27_578)
    )

    val HrithikRoshan = Person(
        name = "Hrithik Roshan",
        profileImage = TmdbProfileImage(path = "/upKrdABAMK7jZevWAoPYI24iKlR.jpg").some(),
        tmdbId = TmdbPersonId(value = 78_749)
    )

    val JonahHill = Person(
        name = "Jonah Hill",
        profileImage = TmdbProfileImage(path = "/1IzQOtqYtU3uYzqcjvzQYtU3uYzqcjvzQ.jpg").some(),
        tmdbId = TmdbPersonId(value = 21_007)
    )

    val JosephGordonLevitt = Person(
        name = "Joseph Gordon-Levitt",
        profileImage = TmdbProfileImage(path = "/dhv9f3AaozOjpvjAwVzOWlmmT2V.jpg").some(),
        tmdbId = TmdbPersonId(value = 24_045)
    )

    val KenWatanabe = Person(
        name = "Ken Watanabe",
        profileImage = TmdbProfileImage(path = "/psAXOYp9SBOXvg6AXzARDedNQ9P.jpg").some(),
        tmdbId = TmdbPersonId(value = 3_899)
    )

    val LeonardoDiCaprio = Person(
        name = "Leonardo DiCaprio",
        profileImage = TmdbProfileImage(path = "/wo2hJpn04vbtmh0B9utCFdsQhxM.jpg").some(),
        tmdbId = TmdbPersonId(value = 6_193)
    )

    val MartinScorsese = Person(
        name = "Martin Scorsese",
        profileImage = TmdbProfileImage(path = "/q9QgL0WKcQZwqQZQXQXx8ZyqJZQQZQ.jpg").some(),
        tmdbId = TmdbPersonId(value = 1_032)
    )

    val SimoneBar = Person(
        name = "Simone Bär",
        profileImage = TmdbProfileImage(path = "/Z1sJ73Rcq8VzUpJzJLPDjiAnAp.jpg").some(),
        tmdbId = TmdbPersonId(value = 4_222)
    )

    val TigerShroff = Person(
        name = "Tiger Shroff",
        profileImage = TmdbProfileImage(path = "/kgvll1RBCcDTs6KJZFDb291AuZb.jpg").some(),
        tmdbId = TmdbPersonId(value = 1_338_512)
    )

    val TomHardy = Person(
        name = "Tom Hardy",
        profileImage = TmdbProfileImage(path = "/yVGF9FvDxTDPhGimTbZNfghpllA.jpg").some(),
        tmdbId = TmdbPersonId(value = 2_524)
    )
}
