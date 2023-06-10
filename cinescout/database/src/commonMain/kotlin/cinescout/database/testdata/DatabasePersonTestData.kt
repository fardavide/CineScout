package cinescout.database.testdata

import cinescout.database.model.DatabasePerson
import cinescout.database.model.id.DatabaseTmdbPersonId

object DatabasePersonTestData {

    val ChristopherNolan = DatabasePerson(
        name = "Christopher Nolan",
        profileImagePath = "/enqbXQXx8ZyqJZQQZQXQXx8ZyqJZQQZQ.jpg",
        tmdbId = DatabaseTmdbPersonId(value = 525)
    )

    val JosephGordonLevitt = DatabasePerson(
        name = "Joseph Gordon-Levitt",
        profileImagePath = "/kvXLhLjybUH0gjyZv6Q6tWj5X4S.jpg",
        tmdbId = DatabaseTmdbPersonId(value = 24_045)
    )

    val LeonardoDiCaprio = DatabasePerson(
        name = "Leonardo DiCaprio",
        profileImagePath = "wo2hJpn04vbtmh0B9utCFdsQhxM.jpg",
        tmdbId = DatabaseTmdbPersonId(value = 6_193)
    )
}
