package cinescout.movies.domain.testdata

import arrow.core.some
import cinescout.movies.domain.model.Person
import cinescout.movies.domain.model.TmdbPersonId
import cinescout.movies.domain.model.TmdbProfileImage

object PersonTestData {

    val ChristopherNolan = Person(
        name = "Christopher Nolan",
        profileImage = TmdbProfileImage(path = "/enqbXQXx8ZyqJZQQZQXQXx8ZyqJZQQZQ.jpg").some(),
        tmdbId = TmdbPersonId(value = 525)
    )

    val JosephGordonLevitt = Person(
        name = "Joseph Gordon-Levitt",
        profileImage = TmdbProfileImage(path = "/kvXLhLjybUH0gjyZv6Q6tWj5X4S.jpg").some(),
        tmdbId = TmdbPersonId(value = 24_045)
    )

    val LeonardoDiCaprio = Person(
        name = "Leonardo DiCaprio",
        profileImage = TmdbProfileImage(path = "/wo2hJpn04vbtmh0B9utCFdsQhxM.jpg").some(),
        tmdbId = TmdbPersonId(value = 6_193)
    )
}
